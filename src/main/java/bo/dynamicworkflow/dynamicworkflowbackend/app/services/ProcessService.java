package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.ForbiddenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.TimestampException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.inputtype.InputTypeNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction.RestrictionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.selectioninputvalue.SelectionInputValueException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage.StageException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst.StageAnalystException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteProcessRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.CompleteProcessResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.ProcessDetailResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.ProcessResponseDto;

import java.util.List;

public interface ProcessService {

    CompleteProcessResponseDto createCompleteProcess(CompleteProcessRequestDto request) throws DepartmentException,
            DepartmentMemberException, ForbiddenException, ProcessException, TimestampException, StageException,
            StageAnalystException, InputException, InputTypeNotFoundException, RestrictionException,
            SelectionInputValueException;

    List<ProcessDetailResponseDto> getAllDetailedProcesses();

    List<ProcessResponseDto> getAllActiveProcessesByDepartmentId(Integer departmentId) throws DepartmentNotFoundException;

}