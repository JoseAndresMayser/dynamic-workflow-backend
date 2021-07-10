package bo.dynamicworkflow.dynamicworkflowbackend.app.rest;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidEmailException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidPasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.UserService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/requesting")
    @ResourceAction(actionCode = ActionCode.USER_REGISTER_REQUESTING)
    public GeneralResponse registerRequestingUser(@RequestBody UserRequestDto request) throws UserException,
            InvalidEmailException, InvalidPasswordException, RoleException, ActionException {
        UserResponseDto response = userService.registerRequestingUser(request);
        return new GeneralResponse(true, response, "Usuario solicitante resgistrado exitosamente.");
    }

    @PostMapping
    @ResourceAction(actionCode = ActionCode.USER_REGISTER)
    public GeneralResponse registerUser(@RequestBody UserWithActionsIdRequestDto request) throws UserException,
            InvalidEmailException, InvalidPasswordException, ActionException {
        UserActionResponseDto response = userService.registerUser(request);
        return new GeneralResponse(true, response, "Usuario resgistrado exitosamente.");
    }

    @PostMapping("/{userId}")
    @ResourceAction(actionCode = ActionCode.USER_UPDATE)
    public GeneralResponse updateUser(@RequestBody UserWithActionsIdRequestDto request,
                                      @PathVariable("userId") Integer userId) throws UserException,
            InvalidEmailException, InvalidPasswordException, ActionException {
        UserActionResponseDto response = userService.updateUser(request, userId);
        return new GeneralResponse(true, response, "Usuario actualizado exitosamente.");
    }

    @PostMapping("/current")
    @ResourceAction(actionCode = ActionCode.USER_CURRENT_UPDATE)
    public GeneralResponse updateCurrentUser(@RequestBody UserRequestDto request) throws UserException,
            InvalidEmailException, InvalidPasswordException {
        UserResponseDto response = userService.updateCurrentUser(request);
        return new GeneralResponse(true, response, "Usuario actual actualizado exitosamente.");
    }

    @GetMapping("/{userId}")
    @ResourceAction(actionCode = ActionCode.USER_GET)
    public GeneralResponse getUserByUserId(@PathVariable("userId") Integer userId) throws UserException {
        UserResponseDto response = userService.getByUserId(userId);
        return new GeneralResponse(true, response, "Usuario obtenido exitosamente.");
    }

    @GetMapping("/current")
    @ResourceAction(actionCode = ActionCode.USER_CURRENT_GET)
    public GeneralResponse getCurrentUser() throws UserException {
        UserResponseDto response = userService.getByUserId(SessionHolder.getCurrentUserId());
        return new GeneralResponse(true, response, "Usuario actual obtenido exitosamente.");
    }

    @GetMapping("/all")
    @ResourceAction(actionCode = ActionCode.USER_GET_ALL)
    public GeneralResponse getAllUsers() {
        List<UserResponseDto> response = userService.getAllUsers();
        return new GeneralResponse(true, response, "Usuarios obtenidos exitosamente.");
    }

}