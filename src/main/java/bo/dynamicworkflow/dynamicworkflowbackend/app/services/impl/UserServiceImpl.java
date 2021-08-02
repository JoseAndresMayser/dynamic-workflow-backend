package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidEmailException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidPasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionWithoutAuthException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.InvalidActionIdListException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.MainRole;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.UserStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.NotificationService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.UserService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.ActionMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.UserMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.PasswordEncryptor;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserActionRepository userActionRepository;
    private final RoleRepository roleRepository;
    private final RoleActionRepository roleActionRepository;
    private final ActionRepository actionRepository;
    private final DepartmentMemberRepository departmentMemberRepository;
    private final NotificationService notificationService;

    private final ActionMapper actionMapper = new ActionMapper();
    private final UserMapper userMapper = new UserMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserActionRepository userActionRepository,
                           RoleRepository roleRepository, RoleActionRepository roleActionRepository,
                           ActionRepository actionRepository, DepartmentMemberRepository departmentMemberRepository,
                           NotificationService notificationService) {
        this.userRepository = userRepository;
        this.userActionRepository = userActionRepository;
        this.roleRepository = roleRepository;
        this.roleActionRepository = roleActionRepository;
        this.actionRepository = actionRepository;
        this.departmentMemberRepository = departmentMemberRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(rollbackOn = {RoleException.class, ActionException.class})
    public UserResponseDto registerRequestingUser(UserRequestDto request) throws UserException, InvalidEmailException,
        InvalidPasswordException, RoleException, ActionException {
        User newRequestingUser = createNewUser(request, true);
        Role role = roleRepository.findByName(MainRole.REQUESTING_USER.name())
            .orElseThrow(() -> new RoleNotFoundException("No se pudo encontrar el Rol \"REQUESTING_USER\""));
        List<RoleAction> roleActions = roleActionRepository.getAllByRoleId(role.getId());
        List<Integer> actionsId = roleActions.stream().map(RoleAction::getActionId).collect(Collectors.toList());
        Integer userId = newRequestingUser.getId();
        registerUserActions(userId, actionsId);
        SessionHolder.setCurrentUserId(userId);
        notificationService.sendRequestingUserSignUpNotification(newRequestingUser);
        return userMapper.toDto(newRequestingUser);
    }

    @Override
    @Transactional(rollbackOn = ActionException.class)
    public UserActionResponseDto registerUser(CompleteUserRequestDto request) throws UserException,
        InvalidEmailException, InvalidPasswordException, ActionException {
        User newUser = createNewUser(request.getUser(), false);
        List<Integer> actionsId = request.getActionsId();
        verifyActionsId(actionsId);
        registerUserActions(newUser.getId(), actionsId);
        List<Action> actions = getActionsByIds(actionsId);
        return new UserActionResponseDto(userMapper.toDto(newUser), actionMapper.toDto(actions));
    }

    @Override
    @Transactional(rollbackOn = ActionException.class)
    public UserActionResponseDto updateUser(CompleteUserRequestDto request, Integer userId) throws UserException,
        InvalidEmailException, InvalidPasswordException, ActionException {
        User user = getUserByUserId(userId);
        User updatedUser = updateUser(user, request.getUser());
        List<Integer> newActionsId = new ArrayList<>(request.getActionsId());
        updateUserActions(newActionsId, user.getId());
        List<Action> actions = getActionsByIds(request.getActionsId());
        return new UserActionResponseDto(userMapper.toDto(updatedUser), actionMapper.toDto(actions));
    }

    @Override
    public UserResponseDto updateCurrentUser(UserRequestDto request) throws UserException, InvalidEmailException,
        InvalidPasswordException {
        User user = getUserByUserId(SessionHolder.getCurrentUserId());
        User updatedCurrentUser = updateUser(user, request);
        return userMapper.toDto(updatedCurrentUser);
    }

    @Override
    public UserResponseDto getByUserId(Integer userId) throws UserException {
        User user = getUserByUserId(userId);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = internalGetAllUsers();
        return userMapper.toDto(users);
    }

    @Override
    public UserActionResponseDto getUserActionsByUserId(Integer userId) throws UserNotFoundException {
        User user = getUserByUserId(userId);
        List<UserAction> userActions = internalGetUserActionsByUserId(user.getId());
        List<Action> actions = userActions.stream().map(UserAction::getAction).collect(Collectors.toList());
        return new UserActionResponseDto(userMapper.toDto(user), actionMapper.toDto(actions));
    }

    @Override
    public List<UserResponseDto> getNonDepartmentBosses() {
        List<Integer> departmentBossesId = departmentMemberRepository.getAllActiveDepartmentBossMembers()
            .stream()
            .map(departmentBossMember -> departmentBossMember.getUser().getId())
            .collect(Collectors.toList());
        List<User> users = internalGetAllUsers();
        users.removeIf(user -> departmentBossesId.contains(user.getId()));
        return userMapper.toDto(users);
    }

    @Override
    public List<UserResponseDto> getNonDepartmentMembers() {
        List<Integer> departmentMembersId = departmentMemberRepository.getAllActiveDepartmentMembers()
            .stream()
            .map(DepartmentMember::getId)
            .collect(Collectors.toList());
        List<User> users = internalGetAllUsers();
        users.removeIf(user -> departmentMembersId.contains(user.getId()));
        return userMapper.toDto(users);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = getOptionalUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                    String.format("No se pudo encontrar un Usuario con el username: %s", username)
                ));
            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
            );
        } catch (Exception ex) {
            throw new UsernameNotFoundException("Usuario no encontrado con el username: ".concat(username), ex);
        }
    }

    private User createNewUser(UserRequestDto request, Boolean isRequestingUser) throws UserException,
        InvalidEmailException, InvalidPasswordException {
        verifyUsername(request.getUsername());
        verifyUserEmail(request.getEmail());
        String password = request.getPassword();
        validatePasswords(password, request.getPasswordConfirmation());
        UserStatus status = getUserStatus(request.getStatus(), isRequestingUser);

        User user = userMapper.toEntity(request);
        String hashedPassword = hashPassword(password);
        user.setPassword(hashedPassword);
        user.setStatus(status);
        Timestamp currentTimestamp = TimeUtility.getCurrentTimestamp();
        user.setCreationTimestamp(currentTimestamp);
        user.setModificationTimestamp(currentTimestamp);

        return userRepository.saveAndFlush(user);
    }

    private void verifyUsername(String username) throws UserException {
        verifyIfUsernameIsNull(username);
        verifyIfUserAlreadyExists(username);
    }

    private void verifyIfUsernameIsNull(String username) throws InvalidUsernameException {
        if (username == null) throw new InvalidUsernameException("El nombre de usuario no debe ser nulo.");
    }

    private void verifyIfUserAlreadyExists(String username) throws UserAlreadyExistsException {
        if (getOptionalUserByUsername(username).isPresent())
            throw new UserAlreadyExistsException(
                String.format("Ya se encuentra registrado un Usuario con el username: %s", username)
            );
    }

    private Optional<User> getOptionalUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void verifyUserEmail(String email) throws InvalidEmailException {
        verifyIfUserEmailIsNull(email);
        verifyIfEmailBelongsToUser(email);
    }

    private void verifyIfUserEmailIsNull(String email) throws InvalidEmailException {
        if (email == null) throw new InvalidEmailException("El correo electrónico del usuario no debe ser nulo.");
    }

    private void verifyIfEmailBelongsToUser(String email) throws InvalidEmailException {
        if (getOptionalUserByEmail(email).isPresent())
            throw new InvalidEmailException(
                String.format("Ya se encuentra registrado un Usuario con el correo electrónico: %s.", email)
            );
    }

    private Optional<User> getOptionalUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    private void validatePasswords(String password, String passwordConfirmation) throws InvalidPasswordException {
        if (password == null || password.isEmpty()) throw new InvalidPasswordException("La contraseña es obligatoria.");
        if (!password.equals(passwordConfirmation))
            throw new InvalidPasswordException("La contraseña y confirmación de contraseña no coinciden.");
    }

    private UserStatus getUserStatus(String userStatus, Boolean isRequestingUser) throws UserStatusException {
        if (isRequestingUser) return UserStatus.ENABLED;
        return getStatusFromString(userStatus);
    }

    private UserStatus getStatusFromString(String userStatus) throws UserStatusException {
        try {
            if (userStatus != null) return UserStatus.valueOf(userStatus);
            throw new UserStatusException("El Estado del usuario no debe ser nulo.");
        } catch (IllegalArgumentException exception) {
            throw new UserStatusException();
        }
    }

    private String hashPassword(String rawPassword) {
        return PasswordEncryptor.hashPassword(rawPassword);
    }

    private void registerUserActions(Integer userId, List<Integer> actionsId) throws ActionNotFoundException {
        List<UserAction> userActions = new ArrayList<>();
        for (Integer actionId : actionsId) {
            Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new ActionNotFoundException(
                    String.format("No se pudo encontrar la Acción con Id: %d", actionId)
                ));
            UserAction newUserAction = new UserAction();
            newUserAction.setUserId(userId);
            newUserAction.setActionId(action.getId());
            userActions.add(newUserAction);
        }
        userActionRepository.saveAll(userActions);
    }

    private void verifyActionsId(List<Integer> actionsId) throws ActionException {
        if (actionsId == null) throw new InvalidActionIdListException("La lista de ID de acciones no debe ser nula.");
        if (actionsId.isEmpty())
            throw new InvalidActionIdListException("La lista de ID de acciones debe contener al menos un elemento.");
        verifyActionsIdWithoutAuthentication(actionsId);
    }

    private void verifyActionsIdWithoutAuthentication(List<Integer> actionsId) throws ActionException {
        List<ActionCode> actionsCodeWithoutAuthentication = ActionCode.actionsWithoutAuth();
        for (ActionCode actionCode : actionsCodeWithoutAuthentication) {
            Action action = actionRepository.findByCode(actionCode)
                .orElseThrow(() -> new ActionNotFoundException(
                    String.format("No se pudo encontrar la Acción con code: %s", actionCode.name())
                ));
            Integer actionId = action.getId();
            if (actionsId.contains(actionId))
                throw new ActionWithoutAuthException(
                    String.format(
                        "El ID de acción: %d pertenece a la Acción con code: %s que no requiere autenticaión",
                        actionId,
                        actionCode.name()
                    )
                );
        }
    }

    private List<Action> getActionsByIds(List<Integer> actionsId) {
        return actionRepository.findAllById(actionsId);
    }

    private User getUserByUserId(Integer userId) throws UserNotFoundException {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(
                String.format("No se pudo encontrar un Usuario con ID: %d", userId)
            ));
    }

    private User updateUser(User userToUpdate, UserRequestDto userRequest) throws UserException, InvalidEmailException,
        InvalidPasswordException {
        String newUsername = userRequest.getUsername();
        verifyIfUsernameIsNull(newUsername);
        if (!newUsername.equals(userToUpdate.getUsername())) verifyIfUserAlreadyExists(newUsername);
        String newEmail = userRequest.getEmail();
        verifyIfUserEmailIsNull(newEmail);
        if (!newEmail.toLowerCase().equals(userToUpdate.getEmail())) verifyIfEmailBelongsToUser(newEmail);

        userToUpdate.setUsername(newUsername);
        String newPassword = userRequest.getPassword();
        if (newPassword != null) {
            String passwordConfirmation = userRequest.getPasswordConfirmation();
            validatePasswords(newPassword, passwordConfirmation);
            String hashedPassword = hashPassword(newPassword);
            userToUpdate.setPassword(hashedPassword);
        }
        userToUpdate.setStatus(getStatusFromString(userRequest.getStatus()));
        userToUpdate.setModificationTimestamp(TimeUtility.getCurrentTimestamp());
        userToUpdate.setNames(userRequest.getNames());
        userToUpdate.setFirstSurname(userRequest.getFirstSurname());
        userToUpdate.setSecondSurname(userRequest.getSecondSurname());
        userToUpdate.setEmail(newEmail);
        userToUpdate.setPhone(userRequest.getPhone());
        userToUpdate.setCode(userRequest.getCode());

        return userRepository.saveAndFlush(userToUpdate);
    }

    private void updateUserActions(List<Integer> newActionsId, Integer userId) throws ActionException {
        verifyActionsId(newActionsId);
        List<UserAction> userCurrentActions = internalGetUserActionsByUserId(userId);
        List<UserAction> userActionsToDelete = userCurrentActions
            .stream()
            .filter(userAction -> !newActionsId.remove(userAction.getActionId()))
            .collect(Collectors.toList());
        userActionRepository.deleteAll(userActionsToDelete);
        registerUserActions(userId, newActionsId);
    }

    private List<UserAction> internalGetUserActionsByUserId(Integer userId) {
        return userActionRepository.getAllByUserId(userId);
    }

    private List<User> internalGetAllUsers() {
        return userRepository.findAll();
    }

}
