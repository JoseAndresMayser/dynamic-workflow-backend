package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.exceptions.GenerateTokenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidPasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.AccessService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.AccessRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RestorePasswordRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdatePasswordRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.AccessResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessService accessService;

    @Autowired
    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @PostMapping("/log-in")
    @ResourceAction(action = ActionCode.ACCESS_LOG_IN)
    public GeneralResponse logIn(@RequestBody AccessRequestDto request) throws UserException, InvalidPasswordException,
            GenerateTokenException {
        AccessResponseDto response = accessService.logIn(request);
        return new GeneralResponse(true, response, "Inicio de sesión exitoso.");
    }

    @PostMapping("/password/restore")
    @ResourceAction(action = ActionCode.ACCESS_PASSWORD_RESTORE)
    public GeneralResponse restorePassword(@RequestBody RestorePasswordRequestDto request) throws UserException {
        accessService.restorePassword(request);
        return new GeneralResponse(true, null, "Contraseña restaurada exitosamente.");
    }

    @PostMapping("/password/update")
    @ResourceAction(action = ActionCode.ACCESS_PASSWORD_UPDATE)
    public GeneralResponse updatePassword(@RequestBody UpdatePasswordRequestDto request) throws UserException,
            InvalidPasswordException {
        accessService.updatePassword(request);
        return new GeneralResponse(true, null, "Contraseña actualizada exitosamente.");
    }

}