package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}