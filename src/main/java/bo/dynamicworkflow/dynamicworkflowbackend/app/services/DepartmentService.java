package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteDepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.CompleteDepartmentResponseDto;

public interface DepartmentService {

    CompleteDepartmentResponseDto registerDepartment(CompleteDepartmentRequestDto request) throws DepartmentException,
            UserException;

}