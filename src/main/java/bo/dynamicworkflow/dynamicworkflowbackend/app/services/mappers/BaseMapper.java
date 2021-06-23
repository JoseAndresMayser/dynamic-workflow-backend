package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<RequestDto, ResponseDto, Entity> {

    public abstract Entity toEntity(RequestDto request);

    public abstract ResponseDto toDto(Entity entity);

    public List<Entity> toEntity(List<RequestDto> requests) {
        return requests.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<ResponseDto> toDto(List<Entity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

}