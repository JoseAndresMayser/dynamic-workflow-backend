package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RoleService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteRoleRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdateRoleActionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @ResourceAction(action = ActionCode.ROLE_REGISTER)
    public GeneralResponse registerRole(@RequestBody CompleteRoleRequestDto request) throws RoleException,
            ActionException {
        RoleActionResponseDto response = roleService.registerRole(request);
        return new GeneralResponse(true, response, "Rol registrado exitosamente.");
    }

    @PostMapping("/{roleId}")
    @ResourceAction(action = ActionCode.ROLE_UPDATE)
    public GeneralResponse updateRole(@RequestBody CompleteRoleRequestDto request,
                                      @PathVariable("roleId") Integer roleId) throws RoleException, ActionException {
        RoleActionResponseDto response = roleService.updateRole(request, roleId);
        return new GeneralResponse(true, response, "Rol actualizado exitosamente.");
    }

    @PostMapping("/{roleId}/actions")
    @ResourceAction(action = ActionCode.ROLE_UPDATE)
    public GeneralResponse updateRoleActions(@RequestBody UpdateRoleActionRequestDto request,
                                             @PathVariable("roleId") Integer roleId) throws RoleException,
            ActionException {
        RoleActionResponseDto response = roleService.updateRoleActions(request, roleId);
        return new GeneralResponse(true, response, "Acciones del rol actualizadas exitosamente.");
    }

    @GetMapping("/{roleId}")
    //@ResourceAction(action = ActionCode.ROLE_GET)
    public GeneralResponse getByRoleId(@PathVariable("roleId") Integer roleId) throws RoleException {
        RoleResponseDto response = roleService.getByRoleId(roleId);
        return new GeneralResponse(true, response, "Rol obtenido exitosamente.");
    }

    @GetMapping("/all")
    @ResourceAction(action = ActionCode.ROLE_GET_ALL)
    public GeneralResponse getAllRoles() {
        List<RoleResponseDto> response = roleService.getAllRoles();
        return new GeneralResponse(true, response, "Roles obtenidos exitosamente.");
    }

    @GetMapping("/{roleId}/actions")
    @ResourceAction(enablerActions = {ActionCode.ROLE_GET_ALL, ActionCode.ROLE_UPDATE, ActionCode.USER_REGISTER,
            ActionCode.USER_UPDATE})
    public GeneralResponse getRoleActionsByRoleId(@PathVariable("roleId") Integer roleId) throws RoleException {
        RoleActionResponseDto response = roleService.getRoleActionsByRoleId(roleId);
        return new GeneralResponse(true, response, "Acciones del rol obtenidas exitosamente.");
    }

    @DeleteMapping("/{roleId}")
    @ResourceAction(action = ActionCode.ROLE_DELETE)
    public GeneralResponse deleteRole(@PathVariable("roleId") Integer roleId) throws RoleNotFoundException {
        roleService.deleteRole(roleId);
        return new GeneralResponse(true, null, "Rol eliminado exitosamente.");
    }

}