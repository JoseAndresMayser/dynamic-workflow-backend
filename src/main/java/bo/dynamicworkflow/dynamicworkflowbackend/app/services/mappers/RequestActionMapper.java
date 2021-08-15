package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.RequestAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RequestActionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestActionResponseDto;

public class RequestActionMapper extends BaseMapper<RequestActionRequestDto, RequestActionResponseDto, RequestAction> {

    @Override
    public RequestAction toEntity(RequestActionRequestDto request) {
        RequestAction requestAction = new RequestAction();
        requestAction.setExecutedAction(request.getExecutedAction());
        requestAction.setCommentary(request.getCommentary());
        return requestAction;
    }

    @Override
    public RequestActionResponseDto toDto(RequestAction requestAction) {
        return new RequestActionResponseDto(
                requestAction.getId(),
                requestAction.getExecutionTimestamp(),
                requestAction.getExecutedAction(),
                requestAction.getCommentary(),
                requestAction.getRequestId(),
                requestAction.getStageAnalystId()
        );
    }

}