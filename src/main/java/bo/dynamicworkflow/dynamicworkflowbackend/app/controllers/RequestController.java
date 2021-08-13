package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RequestService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.NewRequestRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
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

    @GetMapping("/all/current-user")
    @ResourceAction(action = ActionCode.REQUEST_GET_ALL_CURRENT_USER)
    public GeneralResponse getAllRequestsFromCurrentUser() {
        List<RequestResponseDto> response = requestService.getAllRequestsFromCurrentUser();
        return new GeneralResponse(
                true,
                response,
                "Solicitudes del usuario actual obtenidas exitosamente."
        );
    }

    @GetMapping("/pending/current-analyst")
    @ResourceAction(action = ActionCode.REQUEST_GET_ALL_PENDING_CURRENT_ANALYST)
    public GeneralResponse getPendingRequestsForCurrentAnalyst() throws DepartmentMemberNotFoundException {
        List<RequestResponseDto> response = requestService.getPendingRequestsForCurrentAnalyst();
        return new GeneralResponse(
                true,
                response,
                "Solicitudes pendientes del analista actual obtenidas exitosamente."
        );
    }

}