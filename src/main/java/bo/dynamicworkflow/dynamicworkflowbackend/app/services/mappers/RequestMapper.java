package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Request;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestResponseDto;

public class RequestMapper extends BaseMapper<Object, RequestResponseDto, Request> {

    @Override
    public Request toEntity(Object request) {
        throw new UnsupportedOperationException("Function not supported.");
    }

    @Override
    public RequestResponseDto toDto(Request request) {
        return new RequestResponseDto(
                request.getId(),
                request.getShippingTimestamp(),
                request.getFinishTimestamp(),
                request.getStatus(),
                request.getCode(),
                request.getFormPath(),
                request.getProcessId(),
                request.getUserId()
        );
    }

}
