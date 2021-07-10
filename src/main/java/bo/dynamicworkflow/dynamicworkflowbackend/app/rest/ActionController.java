package bo.dynamicworkflow.dynamicworkflowbackend.app.rest;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.ActionService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.ActionDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping("/all")
    @ResourceAction(actionCode = ActionCode.ACTION_GET_ALL)
    public GeneralResponse getAllActions() {
        List<ActionDto> response = actionService.getAllActions();
        return new GeneralResponse(true, response, "Acciones obtenidas exitosamente.");
    }

}