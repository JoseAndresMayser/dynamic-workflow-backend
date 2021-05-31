package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleWithActionsIdRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdateRoleActionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleResponseDto;

import java.util.List;

public interface RoleService {

    RoleActionResponseDto registerRole(RoleWithActionsIdRequestDto request) throws RoleException, ActionException;

    RoleResponseDto updateRole(RoleRequestDto request, Integer roleId) throws RoleException;

    RoleActionResponseDto updateRoleActions(UpdateRoleActionRequestDto request, Integer roleId) throws RoleException,
            ActionException;

    RoleResponseDto getByRoleId(Integer roleId) throws RoleException;

    List<RoleResponseDto> getAllRoles();

    RoleActionResponseDto getRoleActionsByRoleId(Integer roleId) throws RoleException;

}