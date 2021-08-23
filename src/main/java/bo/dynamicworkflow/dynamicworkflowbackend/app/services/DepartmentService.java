package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.UserAlreadyDepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteDepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdateDepartmentMembersRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.CompleteDepartmentResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DepartmentResponseDto;

import java.util.List;

public interface DepartmentService {

    CompleteDepartmentResponseDto registerCompleteDepartment(CompleteDepartmentRequestDto request)
            throws DepartmentException, UserAlreadyDepartmentMemberException, UserException;

    DepartmentResponseDto updateDepartment(DepartmentRequestDto request, Integer departmentId)
            throws DepartmentException;

    CompleteDepartmentResponseDto updateDepartmentMembers(UpdateDepartmentMembersRequestDto request,
                                                          Integer departmentId) throws DepartmentException,
            UserNotFoundException, DepartmentMemberException;

    CompleteDepartmentResponseDto getCompleteDepartmentById(Integer departmentId) throws DepartmentNotFoundException,
            DepartmentMemberNotFoundException;

    List<DepartmentResponseDto> getAllDepartmentsForCurrentUser() throws DepartmentMemberNotFoundException,
            DepartmentNotFoundException;

    List<DepartmentResponseDto> getDepartmentWithDescendantsById(Integer departmentId)
            throws DepartmentNotFoundException;

    DepartmentResponseDto getRootDepartment() throws DepartmentNotFoundException;

    DepartmentResponseDto getDepartmentById(Integer departmentId) throws DepartmentNotFoundException;

}