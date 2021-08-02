package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.TokenManager;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.dto.TokenRequest;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.dto.TokenResponse;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.enums.TimeInstanceType;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.exceptions.GenerateTokenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidPasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.DisabledUserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Action;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.UserAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.UserStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.NotificationService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.UserActionRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.UserRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.AccessService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.UserAccountDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.AccessRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RestorePasswordRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdatePasswordRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.AccessResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.ActionMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.PasswordEncryptor;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.PasswordGenerator;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessServiceImpl implements AccessService {

    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final UserActionRepository userActionRepository;
    private final NotificationService notificationService;

    private final ActionMapper actionMapper = new ActionMapper();

    @Autowired
    public AccessServiceImpl(UserRepository userRepository, TokenManager tokenManager,
                             UserActionRepository userActionRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
        this.userActionRepository = userActionRepository;
        this.notificationService = notificationService;
    }

    @Override
    public AccessResponseDto logIn(AccessRequestDto request) throws UserException, InvalidPasswordException,
            GenerateTokenException {
        User user = getUserForLogIn(request.getUsername());
        UserStatus userStatus = user.getStatus();
        if (userStatus.equals(UserStatus.DISABLED)) throw new DisabledUserException();
        verifyPasswordForLogIn(request.getPassword(), user.getPassword());

        String token = generateToken(user);
        Integer userId = user.getId();
        List<Action> actions = getActionsForUserByUserId(userId);

        return new AccessResponseDto(
                userId,
                user.getUsername(),
                userStatus,
                user.fullName(),
                user.getEmail(),
                user.getPhone(),
                user.getCode(),
                token,
                actionMapper.toDto(actions)
        );
    }

    @Override
    public void restorePassword(RestorePasswordRequestDto request) throws UserException {
        String email = request.getUserEmail();
        User user = getUserForRestorePassword(email);
        if (user.getStatus().equals(UserStatus.DISABLED)) throw new DisabledUserException();
        String temporalPassword = generatePassword();
        String hashedPassword = hashPassword(temporalPassword);
        user.setPassword(hashedPassword);
        user.setStatus(UserStatus.RESTORE_PASSWORD);
        user.setModificationTimestamp(TimeUtility.getCurrentTimestamp());
        User updatedUser = userRepository.saveAndFlush(user);
        notificationService.sendRestorePasswordNotification(updatedUser.getEmail(), temporalPassword);
    }

    @Override
    public void updatePassword(UpdatePasswordRequestDto request) throws UserException, InvalidPasswordException {
        User user = getUserByUserId(SessionHolder.getCurrentUserId());
        validateCurrentPassword(request.getCurrentPassword(), user.getPassword());
        String newPassword = request.getNewPassword();
        validatePasswords(newPassword, request.getNewPasswordConfirmation());
        String hashedPassword = hashPassword(newPassword);
        user.setPassword(hashedPassword);
        if (user.getStatus().equals(UserStatus.RESTORE_PASSWORD)) user.setStatus(UserStatus.ENABLED);
        user.setModificationTimestamp(TimeUtility.getCurrentTimestamp());
        User updatedUser = userRepository.saveAndFlush(user);
        notificationService.sendUpdatedPasswordNotification(updatedUser.getEmail());
    }

    private User getUserForLogIn(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("El usuario no se encuentra registrado."));
    }

    private void verifyPasswordForLogIn(String plainTextPassword, String hashedPassword)
            throws InvalidPasswordException {
        if (!PasswordEncryptor.checkPassword(plainTextPassword, hashedPassword)) throw new InvalidPasswordException();
    }

    private String generateToken(User user) throws GenerateTokenException {
        String username = user.getUsername();
        UserAccountDto userAccountDto = new UserAccountDto(
                user.getId(),
                username,
                user.getNames(),
                user.getFirstSurname(),
                user.getSecondSurname(),
                user.getEmail(),
                user.getCode()
        );

        TokenRequest<UserAccountDto> tokenRequest = new TokenRequest<>(
                username,
                TimeInstanceType.YEAR,
                1,
                userAccountDto
        );

        TokenResponse tokenResponse = tokenManager.generateTokenResponse(tokenRequest);
        return tokenResponse.getToken();
    }

    private List<Action> getActionsForUserByUserId(Integer userId) {
        List<UserAction> userActions = userActionRepository.getAllByUserId(userId);
        return userActions.stream().map(UserAction::getAction).collect(Collectors.toList());
    }

    private User getUserForRestorePassword(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("El correo electrónico: %s no pertenece a un usuario registrado.", email)
                ));
    }

    private String generatePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator(10);
        return new String(passwordGenerator.get());
    }

    private String hashPassword(String rawPassword) {
        return PasswordEncryptor.hashPassword(rawPassword);
    }

    private User getUserByUserId(Integer userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("No se pudo encontrar un Usuario con ID: %d", userId)
                ));
    }

    private void validateCurrentPassword(String currentPasswordSent, String storedPassword)
            throws InvalidPasswordException {
        if (!PasswordEncryptor.checkPassword(currentPasswordSent, storedPassword))
            throw new InvalidPasswordException("La contraseña actual es inválida.");
    }

    private void validatePasswords(String password, String passwordConfirmation) throws InvalidPasswordException {
        if (password == null || password.isEmpty()) throw new InvalidPasswordException("La contraseña es obligatoria.");
        if (!password.equals(passwordConfirmation))
            throw new InvalidPasswordException("La contraseña y confirmación de contraseña no coinciden.");
    }

}