package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DeleteFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.ForbiddenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.InvalidCertificateFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.DepartmentMemberDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DigitalCertificateRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DigitalCertificateResponseDto;

import java.util.List;

public interface DepartmentMemberService {

    DigitalCertificateResponseDto uploadDigitalCertificate(DigitalCertificateRequestDto request,
                                                           Integer departmentMemberId)
            throws DepartmentMemberNotFoundException, ForbiddenException, InvalidCertificateFileException,
            DirectoryCreationException, SaveFileException, DeleteFileException;

    List<DepartmentMemberDto> getAllDepartmentMembersByDepartmentId(Integer departmentId)
            throws DepartmentNotFoundException;

}