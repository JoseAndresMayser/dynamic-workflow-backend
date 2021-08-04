package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.UserAlreadyDepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.DepartmentService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteDepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdateDepartmentMembersRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.CompleteDepartmentResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DepartmentResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/complete")
    @ResourceAction(action = ActionCode.DEPARTMENT_REGISTER)
    public GeneralResponse registerCompleteDepartment(@RequestBody CompleteDepartmentRequestDto request)
            throws DepartmentException, UserAlreadyDepartmentMemberException, UserException {
        CompleteDepartmentResponseDto response = departmentService.registerCompleteDepartment(request);
        return new GeneralResponse(true, response, "Departamento registrado exitosamente.");
    }

    @PostMapping("/{departmentId}")
    @ResourceAction(action = ActionCode.DEPARTMENT_UPDATE)
    public GeneralResponse updateDepartment(@RequestBody DepartmentRequestDto request,
                                            @PathVariable("departmentId") Integer departmentId)
            throws DepartmentException {
        DepartmentResponseDto response = departmentService.updateDepartment(request, departmentId);
        return new GeneralResponse(true, response, "Departamento actualizado exitosamente.");
    }

    @PostMapping("/{departmentId}/members")
    @ResourceAction(action = ActionCode.DEPARTMENT_UPDATE_MEMBERS)
    public GeneralResponse updateDepartmentMembers(@RequestBody UpdateDepartmentMembersRequestDto request,
                                                   @PathVariable("departmentId") Integer departmentId)
            throws DepartmentException, UserNotFoundException, DepartmentMemberException {
        CompleteDepartmentResponseDto response = departmentService.updateDepartmentMembers(request, departmentId);
        return new GeneralResponse(
                true,
                response,
                "Miembros del Departamento actualizados exitosamente."
        );
    }

    @GetMapping("/{departmentId}/complete")
    @ResourceAction(action = ActionCode.DEPARTMENT_GET)
    public GeneralResponse getCompleteDepartmentById(@PathVariable("departmentId") Integer departmentId)
            throws DepartmentNotFoundException, DepartmentMemberNotFoundException, UserNotFoundException {
        CompleteDepartmentResponseDto response = departmentService.getCompleteDepartmentById(departmentId);
        return new GeneralResponse(true, response, "Departamento obtenido exitosamente.");
    }

    @GetMapping("/current-user/all")
    @ResourceAction(action = ActionCode.DEPARTMENT_GET_ALL)
    public GeneralResponse getAllDepartmentsForCurrentUser() throws DepartmentMemberNotFoundException,
            DepartmentNotFoundException {
        List<DepartmentResponseDto> response = departmentService.getAllDepartmentsForCurrentUser();
        return new GeneralResponse(true, response, "Departamentos obtenidos exitosamente.");
    }

    @GetMapping("/root")
    @ResourceAction(enablerActions = ActionCode.REQUEST_CREATE)
    public GeneralResponse getRootDepartment() throws DepartmentNotFoundException {
        DepartmentResponseDto response = departmentService.getRootDepartment();
        return new GeneralResponse(true, response, "Departamento raiz obtenido exitosamente.");
    }

}