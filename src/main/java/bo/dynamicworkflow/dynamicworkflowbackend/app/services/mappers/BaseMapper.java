package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapper<RequestDto, ResponseDto, Entity> {

    public abstract Entity toEntity(RequestDto request);

    public abstract ResponseDto toDto(Entity entity);

    public List<Entity> toEntity(List<RequestDto> requests) {
        List<Entity> entities = new ArrayList<>();
        for (RequestDto request : requests) {
            entities.add(toEntity(request));
        }
        return entities;
    }

    public List<ResponseDto> toDto(List<Entity> entities) {
        List<ResponseDto> responses = new ArrayList<>();
        for (Entity entity : entities) {
            responses.add(toDto(entity));
        }
        return responses;
    }

}