package bo.dynamicworkflow.dynamicworkflowbackend.app.controllers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.InputTypeService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.InputTypeDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/input-types")
public class InputTypeController {

    private final InputTypeService inputTypeService;

    @Autowired
    public InputTypeController(InputTypeService inputTypeService) {
        this.inputTypeService = inputTypeService;
    }

    @GetMapping("/all")
    @ResourceAction(enablerActions = {ActionCode.PROCESS_CREATE})
    public GeneralResponse getAllInputTypes() {
        List<InputTypeDto> response = inputTypeService.getAllInputTypes();
        return new GeneralResponse(true, response, "Tipos de Entradas obtenidos exitosamente.");
    }

}