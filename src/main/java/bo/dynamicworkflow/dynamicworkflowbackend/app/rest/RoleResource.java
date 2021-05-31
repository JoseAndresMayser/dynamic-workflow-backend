package bo.dynamicworkflow.dynamicworkflowbackend.app.rest;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RoleService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleWithActionsIdRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdateRoleActionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleResource {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public GeneralResponse registerRole(@RequestBody RoleWithActionsIdRequestDto request) throws RoleException,
            ActionException {
        RoleActionResponseDto response = roleService.registerRole(request);
        return new GeneralResponse(true, response, "Rol registrado exitosamente.");
    }

    @PostMapping("/{roleId}/update")
    public GeneralResponse updateRole(@RequestBody RoleRequestDto request, @PathVariable("roleId") Integer roleId)
            throws RoleException {
        RoleResponseDto response = roleService.updateRole(request, roleId);
        return new GeneralResponse(true, response, "Rol actualizado exitosamente.");
    }

    @PostMapping("/{roleId}/actions")
    public GeneralResponse updateRoleActions(@RequestBody UpdateRoleActionRequestDto request,
                                             @PathVariable("roleId") Integer roleId) throws RoleException,
            ActionException {
        RoleActionResponseDto response = roleService.updateRoleActions(request, roleId);
        return new GeneralResponse(true, response, "Acciones del rol actualizadas exitosamente.");
    }

    @GetMapping("/{roleId}")
    public GeneralResponse getByRoleId(@PathVariable("roleId") Integer roleId) throws RoleException {
        RoleResponseDto response = roleService.getByRoleId(roleId);
        return new GeneralResponse(true, response, "Rol obtenido exitosamente.");
    }

    @GetMapping("/all")
    public GeneralResponse getAllRoles() {
        List<RoleResponseDto> response = roleService.getAllRoles();
        return new GeneralResponse(true, response, "Roles obtenidos exitosamente.");
    }

    @GetMapping("/{roleId}/actions")
    public GeneralResponse getRoleActionsByRoleId(@PathVariable("roleId") Integer roleId) throws RoleException {
        RoleActionResponseDto response = roleService.getRoleActionsByRoleId(roleId);
        return new GeneralResponse(true, response, "Acciones del rol obtenidas exitosamente.");
    }

}