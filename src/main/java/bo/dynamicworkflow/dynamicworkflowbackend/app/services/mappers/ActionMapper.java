package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Action;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.ActionDto;

public class ActionMapper extends BaseMapper<ActionDto, ActionDto, Action> {

    @Override
    public Action toEntity(ActionDto request) {
        throw new UnsupportedOperationException("Function not supported.");
    }

    @Override
    public ActionDto toDto(Action action) {
        return new ActionDto(action.getId(), action.getCode(), action.getDescription());
    }

}