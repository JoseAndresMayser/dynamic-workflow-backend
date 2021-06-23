package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.ActionDto;

import java.util.List;

public interface ActionService {

    List<ActionDto> getAllActions();

}