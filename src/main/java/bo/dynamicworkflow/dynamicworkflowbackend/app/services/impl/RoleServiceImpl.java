package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionWithoutAuthException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.InvalidActionsIdListException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Action;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Role;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.RoleAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.MainRole;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.ActionRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.RoleActionRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.RoleRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RoleService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteRoleRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdateRoleActionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.ActionMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;
    private final RoleActionRepository roleActionRepository;

    private final RoleMapper roleMapper = new RoleMapper();
    private final ActionMapper actionMapper = new ActionMapper();

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ActionRepository actionRepository,
                           RoleActionRepository roleActionRepository) {
        this.roleRepository = roleRepository;
        this.actionRepository = actionRepository;
        this.roleActionRepository = roleActionRepository;
    }

    @Override
    @Transactional(rollbackOn = ActionException.class)
    public RoleActionResponseDto registerRole(CompleteRoleRequestDto request) throws RoleException,
            ActionException {
        RoleRequestDto roleRequest = request.getRole();
        verifyRoleName(roleRequest.getName());
        Role role = roleMapper.toEntity(roleRequest);
        Role registeredRole = roleRepository.saveAndFlush(role);
        List<Integer> actionsId = request.getActionsId();
        verifyActionsId(actionsId);
        registerRoleActions(actionsId, registeredRole.getId());
        List<Action> actions = getActionsByIds(actionsId);
        return new RoleActionResponseDto(roleMapper.toDto(registeredRole), actionMapper.toDto(actions));
    }

    @Override
    @Transactional(rollbackOn = ActionException.class)
    public RoleActionResponseDto updateRole(CompleteRoleRequestDto request, Integer roleId) throws RoleException,
            ActionException {
        Role role = getRoleByRoleId(roleId);
        String roleName = role.getName();
        verifyIfIsMainRole(roleName);
        RoleRequestDto roleRequest = request.getRole();
        String newRoleName = roleRequest.getName();
        verifyIfRoleNameIsNull(newRoleName);
        if (!newRoleName.toUpperCase().equals(roleName)) verifyIfRoleAlreadyExists(newRoleName);
        role.setName(newRoleName);
        role.setDescription(roleRequest.getDescription());
        Role updatedRole = roleRepository.saveAndFlush(role);
        List<Integer> newActionsId = new ArrayList<>(request.getActionsId());
        updateRoleActions(newActionsId, role.getId());
        List<Action> actions = getActionsByIds(request.getActionsId());
        return new RoleActionResponseDto(roleMapper.toDto(updatedRole), actionMapper.toDto(actions));
    }

    @Override
    public RoleActionResponseDto updateRoleActions(UpdateRoleActionRequestDto request, Integer roleId)
            throws RoleException, ActionException {
        Role role = getRoleByRoleId(roleId);
        verifyIfIsMainRole(role.getName());
        List<Integer> newActionsId = new ArrayList<>(request.getActionsId());
        updateRoleActions(newActionsId, role.getId());
        List<Action> actions = getActionsByIds(request.getActionsId());
        return new RoleActionResponseDto(roleMapper.toDto(role), actionMapper.toDto(actions));
    }

    @Override
    public RoleResponseDto getByRoleId(Integer roleId) throws RoleException {
        return roleMapper.toDto(getRoleByRoleId(roleId));
    }

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleMapper.toDto(roleRepository.findAll());
    }

    @Override
    public RoleActionResponseDto getRoleActionsByRoleId(Integer roleId) throws RoleException {
        Role role = getRoleByRoleId(roleId);
        List<RoleAction> roleActions = internalGetRoleActionsByRoleId(roleId);
        List<Action> actions = roleActions.stream().map(RoleAction::getAction).collect(Collectors.toList());
        return new RoleActionResponseDto(roleMapper.toDto(role), actionMapper.toDto(actions));
    }

    @Override
    public void deleteRole(Integer roleId) throws RoleNotFoundException {
        Role role = getRoleByRoleId(roleId);
        roleRepository.delete(role);
    }

    private void verifyRoleName(String roleName) throws RoleException {
        verifyIfRoleNameIsNull(roleName);
        verifyIfRoleAlreadyExists(roleName);
    }

    private void verifyIfRoleNameIsNull(String roleName) throws InvalidRoleNameException {
        if (roleName == null) throw new InvalidRoleNameException("El nombre del Rol no debe ser nulo.");
    }

    private void verifyIfRoleAlreadyExists(String roleName) throws RoleAlreadyExistsException {
        if (roleRepository.findByName(roleName.toUpperCase()).isPresent())
            throw new RoleAlreadyExistsException(
                    String.format("Ya se encuentra registrado un Rol con el nombre: %s", roleName.toUpperCase())
            );
    }

    private void verifyActionsId(List<Integer> actionsId) throws ActionException {
        if (actionsId == null) throw new InvalidActionsIdListException("La lista de ID de acciones no debe ser nula.");
        if (actionsId.isEmpty())
            throw new InvalidActionsIdListException("La lista de ID de acciones debe contener al menos un elemento.");
        verifyActionsIdWithoutAuthentication(actionsId);
    }

    private void verifyActionsIdWithoutAuthentication(List<Integer> actionsId) throws ActionException {
        List<ActionCode> actionsCodeWithoutAuthentication = ActionCode.getActionsCodeWithoutAuth();
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

    private void registerRoleActions(List<Integer> actionsId, Integer roleId) throws ActionNotFoundException {
        List<RoleAction> roleActions = new ArrayList<>();
        for (Integer actionId : actionsId) {
            Action action = actionRepository.findById(actionId)
                    .orElseThrow(() -> new ActionNotFoundException(
                            String.format("No se pudo encontrar la Acción con Id: %d", actionId)
                    ));
            RoleAction newRoleAction = new RoleAction();
            newRoleAction.setRoleId(roleId);
            newRoleAction.setActionId(action.getId());
            roleActions.add(newRoleAction);
        }
        roleActionRepository.saveAll(roleActions);
    }

    private List<Action> getActionsByIds(List<Integer> actionsId) {
        return actionRepository.findAllById(actionsId);
    }

    private void verifyIfIsMainRole(String roleName) throws UpdateMainRoleException {
        try {
            MainRole.valueOf(roleName);
            throw new UpdateMainRoleException();
        } catch (IllegalArgumentException ignored) {
        }
    }

    private void updateRoleActions(List<Integer> newActionsId, Integer roleId) throws ActionException {
        verifyActionsId(newActionsId);
        List<RoleAction> roleCurrentActions = internalGetRoleActionsByRoleId(roleId);
        List<RoleAction> roleActionsToDelete = roleCurrentActions
                .stream()
                .filter(roleAction -> !newActionsId.remove(roleAction.getActionId()))
                .collect(Collectors.toList());
        roleActionRepository.deleteAll(roleActionsToDelete);
        registerRoleActions(newActionsId, roleId);
    }

    private Role getRoleByRoleId(Integer roleId) throws RoleNotFoundException {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(
                        String.format("No se pudo encontrar el Rol con ID: %d", roleId)
                ));
    }

    private List<RoleAction> internalGetRoleActionsByRoleId(Integer roleId) {
        return roleActionRepository.getAllByRoleId(roleId);
    }

}