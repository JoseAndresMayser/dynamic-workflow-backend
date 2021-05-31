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
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.ActionDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleWithActionsIdRequestDto;
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

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private RoleActionRepository roleActionRepository;

    private final RoleMapper roleMapper = new RoleMapper();
    private final ActionMapper actionMapper = new ActionMapper();

    @Override
    @Transactional(rollbackOn = ActionException.class)
    public RoleActionResponseDto registerRole(RoleWithActionsIdRequestDto request) throws RoleException,
            ActionException {
        RoleRequestDto roleRequest = request.getRole();
        verifyRoleName(roleRequest.getName());
        Role role = roleMapper.toEntity(roleRequest);
        Role registeredRole = roleRepository.saveAndFlush(role);
        List<Integer> actionsId = request.getActionsId();
        verifyActionsId(actionsId);
        Integer roleId = registeredRole.getId();
        List<Action> actions = registerRoleActions(actionsId, roleId);
        return new RoleActionResponseDto(roleMapper.toDto(registeredRole), actionMapper.toDto(actions));
    }

    @Override
    public RoleResponseDto updateRole(RoleRequestDto request, Integer roleId) throws RoleException {
        Role role = getRoleByRoleId(roleId);
        String roleName = role.getName();
        verifyIfIsMainRole(roleName);
        String newRoleName = request.getName();
        verifyIfRoleNameIsNull(newRoleName);
        if (!newRoleName.toUpperCase().equals(roleName)) verifyIfRoleAlreadyExists(newRoleName);
        role.setName(newRoleName);
        role.setDescription(request.getDescription());
        Role updatedRole = roleRepository.saveAndFlush(role);
        return roleMapper.toDto(updatedRole);
    }

    @Override
    public RoleActionResponseDto updateRoleActions(UpdateRoleActionRequestDto request, Integer roleId)
            throws RoleException, ActionException {
        Role role = getRoleByRoleId(roleId);
        List<Integer> newActionsId = request.getActionsId();
        verifyActionsId(newActionsId);
        List<RoleAction> roleCurrentActions = internalGetRoleActionsByRoleId(role.getId());
        List<RoleAction> roleActionsToDelete = roleCurrentActions
                .stream()
                .filter(roleAction -> !newActionsId.remove(roleAction.getActionId()))
                .collect(Collectors.toList());
        roleActionRepository.deleteAll(roleActionsToDelete);
        List<Action> actions = registerRoleActions(newActionsId, role.getId());
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
        return new RoleActionResponseDto(roleMapper.toDto(role), getRoleActions(role.getId()));
    }

    private void verifyRoleName(String roleName) throws RoleException {
        verifyIfRoleNameIsNull(roleName);
        verifyIfRoleAlreadyExists(roleName);
    }

    private void verifyIfRoleNameIsNull(String roleName) throws InvalidRoleNameException {
        if (roleName == null) throw new InvalidRoleNameException("El nombre del Rol no debe ser nulo.");
    }

    private void verifyIfRoleAlreadyExists(String roleName) throws RoleAlreadyExistsException {
        if (roleRepository.getRoleByName(roleName.toUpperCase()).isPresent())
            throw new RoleAlreadyExistsException(
                    String.format("Ya se encuentra registrado un Rol con el nombre: %s", roleName.toUpperCase())
            );
    }

    private void verifyActionsId(List<Integer> actionsId) throws ActionException {
        if (actionsId == null) throw new InvalidActionsIdListException("La lista de ID de acciones no debe ser nula.");
        if (actionsId.isEmpty())
            throw new InvalidActionsIdListException("La lista de ID de acciones debe contener al menos un ID.");
        verifyActionsIdWithoutAuthentication(actionsId);
    }

    private void verifyActionsIdWithoutAuthentication(List<Integer> actionsId) throws ActionException {
        List<ActionCode> actionsCodeWithoutAuthentication = ActionCode.getActionsCodeWithoutAuthentication();
        for (ActionCode actionCode : actionsCodeWithoutAuthentication) {
            Action action = actionRepository.getActionByCode(actionCode)
                    .orElseThrow(() -> new ActionNotFoundException(
                                    String.format("No se pudo encontrar la Acción con code: %s", actionCode.name())
                            )
                    );
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

    private List<Action> registerRoleActions(List<Integer> actionsId, Integer roleId) throws ActionException {
        List<RoleAction> roleActions = new ArrayList<>();
        List<Action> actions = new ArrayList<>();
        for (Integer actionId : actionsId) {
            Action action = actionRepository.findById(actionId)
                    .orElseThrow(() ->
                            new ActionNotFoundException(
                                    String.format("No se pudo encontrar la Acción con Id: %d", actionId)
                            )
                    );
            RoleAction newRoleAction = new RoleAction();
            newRoleAction.setRoleId(roleId);
            newRoleAction.setActionId(action.getId());
            roleActions.add(newRoleAction);
            actions.add(action);
        }
        roleActionRepository.saveAll(roleActions);
        return actions;
    }

    private List<ActionDto> getRoleActions(Integer roleId) {
        List<RoleAction> roleActions = roleActionRepository.getAllByRoleId(roleId);
        List<Action> actions = roleActions.stream().map(RoleAction::getAction).collect(Collectors.toList());
        return actionMapper.toDto(actions);
    }

    private Role getRoleByRoleId(Integer roleId) throws RoleNotFoundException {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(
                                String.format("No se pudo encontrar un Rol con ID: %d", roleId)
                        )
                );
    }

    private void verifyIfIsMainRole(String roleName) throws UpdateMainRoleException {
        MainRole[] mainRoles = MainRole.values();
        for (MainRole mainRole : mainRoles)
            if (roleName.equals(mainRole.name())) throw new UpdateMainRoleException();
    }

    private List<RoleAction> internalGetRoleActionsByRoleId(Integer roleId) {
        return roleActionRepository.getAllByRoleId(roleId);
    }

}