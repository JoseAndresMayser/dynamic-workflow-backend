package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DeleteFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.ForbiddenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.DigitalCertificateNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.InvalidCertificateFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.DepartmentMemberService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.DepartmentMemberDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DigitalCertificateRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DigitalCertificateResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department-members")
public class DepartmentMemberController {

    private final DepartmentMemberService departmentMemberService;

    @Autowired
    public DepartmentMemberController(DepartmentMemberService departmentMemberService) {
        this.departmentMemberService = departmentMemberService;
    }

    @PostMapping("/{departmentMemberId}/digital-certificate")
    @ResourceAction(action = ActionCode.DEPARTMENT_MEMBER_UPLOAD_CERTIFICATE)
    public GeneralResponse uploadDigitalCertificate(@RequestBody DigitalCertificateRequestDto request,
                                                    @PathVariable("departmentMemberId") Integer departmentMemberId)
            throws DepartmentMemberNotFoundException, ForbiddenException, InvalidCertificateFileException,
            DirectoryCreationException, SaveFileException, DeleteFileException {
        DigitalCertificateResponseDto response =
                departmentMemberService.uploadDigitalCertificate(request, departmentMemberId);
        return new GeneralResponse(true, response, "Certificado digital subido exitosamente.");
    }

    @GetMapping("/{departmentId}/all")
    @ResourceAction(enablerActions = {ActionCode.PROCESS_CREATE})
    public GeneralResponse getAllDepartmentMembersByDepartmentId(@PathVariable("departmentId") Integer departmentId)
            throws DepartmentNotFoundException {
        List<DepartmentMemberDto> response =
                departmentMemberService.getAllDepartmentMembersByDepartmentId(departmentId);
        return new GeneralResponse(true, response, "Miembros del Departamento obtenidos exitosamente.");
    }

    @GetMapping("/{departmentMemberId}/digital-certificate")
    @ResourceAction(enablerActions = {ActionCode.DEPARTMENT_GET})
    public GeneralResponse getDigitalCertificateByDepartmentMemberId(
            @PathVariable("departmentMemberId") Integer departmentMemberId
    ) throws DepartmentMemberNotFoundException, ForbiddenException, DigitalCertificateNotFoundException {
        DigitalCertificateResponseDto response =
                departmentMemberService.getDigitalCertificateByDepartmentMemberId(departmentMemberId);
        return new GeneralResponse(true, response, "Certificado digital obtenido exitosamente.");
    }

}