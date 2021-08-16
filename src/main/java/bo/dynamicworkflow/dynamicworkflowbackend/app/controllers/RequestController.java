package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.DigitalCertificateNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.InvalidDigitalCertificatePasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.InactiveProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.requeststage.RequestStageException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst.StageAnalystException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RequestService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.NewRequestRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RequestActionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResourceAction(action = ActionCode.REQUEST_REGISTER)
    public GeneralResponse registerRequest(@RequestBody NewRequestRequestDto request) throws ProcessException,
            InputException, DirectoryCreationException, SaveFileException, RequestException, UserNotFoundException {
        RequestResponseDto response = requestService.registerRequest(request);
        return new GeneralResponse(true, response, "Solicitud registrada exitosamente.");
    }

    @PostMapping("/{requestId}/execute-action")
    @ResourceAction(action = ActionCode.REQUEST_EXECUTE_ACTION)
    public GeneralResponse executeActionOnRequest(@RequestBody RequestActionRequestDto request,
                                                  @PathVariable("requestId") Integer requestId)
            throws RequestException, InvalidDigitalCertificatePasswordException, StageAnalystException,
            InactiveProcessException, DepartmentMemberNotFoundException, RequestStageException,
            DigitalCertificateNotFoundException {
        RequestActionResponseDto response = requestService.executeActionOnRequest(request, requestId);
        return new GeneralResponse(true, response, "Acción ejecutada sobre la solicitud exitosamente.");
    }

    @GetMapping("/all/current-user")
    @ResourceAction(action = ActionCode.REQUEST_GET_ALL_CURRENT_USER)
    public GeneralResponse getAllRequestsForCurrentUser() {
        List<RequestResponseDto> response = requestService.getAllRequestsForCurrentUser();
        return new GeneralResponse(
                true,
                response,
                "Solicitudes del usuario actual obtenidas exitosamente."
        );
    }

    @GetMapping("/pending/current-analyst")
    @ResourceAction(enablerActions = {ActionCode.REQUEST_EXECUTE_ACTION})
    public GeneralResponse getPendingRequestsForCurrentAnalyst() throws DepartmentMemberNotFoundException {
        List<RequestResponseDto> response = requestService.getPendingRequestsForCurrentAnalyst();
        return new GeneralResponse(
                true,
                response,
                "Solicitudes pendientes del analista actual obtenidas exitosamente."
        );
    }

    @GetMapping("/finished/current-analyst")
    @ResourceAction(enablerActions = {ActionCode.REQUEST_EXECUTE_ACTION})
    public GeneralResponse getFinishedRequestsForCurrentAnalyst() throws DepartmentMemberNotFoundException {
        List<RequestResponseDto> response = requestService.getFinishedRequestsForCurrentAnalyst();
        return new GeneralResponse(
                true,
                response,
                "Solicitudes finalizadas del analista actual obtenidas exitosamente."
        );
    }

}