package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.ForbiddenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.TimestampException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.inputtype.InputTypeNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction.RestrictionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.selectioninputvalue.SelectionInputValueException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage.StageException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst.StageAnalystException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.ProcessService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteProcessRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.CompleteProcessResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.ProcessDetailResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    private final ProcessService processService;

    @Autowired
    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @PostMapping("/complete")
    @ResourceAction(action = ActionCode.PROCESS_CREATE)
    public GeneralResponse createProcess(@RequestBody CompleteProcessRequestDto request) throws DepartmentException,
            DepartmentMemberException, ForbiddenException, ProcessException, TimestampException, StageException,
            StageAnalystException, InputException, InputTypeNotFoundException, RestrictionException,
            SelectionInputValueException {
        CompleteProcessResponseDto response = processService.createCompleteProcess(request);
        return new GeneralResponse(true, response, "Proceso creado exitosamente.");
    }

    @GetMapping("/all")
    @ResourceAction(action = ActionCode.PROCESS_GET_ALL)
    public GeneralResponse getAllDetailedProcesses() {
        List<ProcessDetailResponseDto> response = processService.getAllDetailedProcesses();
        return new GeneralResponse(true, response, "Procesos detallados obtenidos exitosamente.");
    }

}