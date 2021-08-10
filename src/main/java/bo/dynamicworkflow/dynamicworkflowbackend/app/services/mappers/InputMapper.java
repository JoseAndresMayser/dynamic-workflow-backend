package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Input;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.InputRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.InputResponseDto;

public class InputMapper extends BaseMapper<InputRequestDto, InputResponseDto, Input> {

    @Override
    public Input toEntity(InputRequestDto request) {
        Input input = new Input();
        input.setName(request.getName());
        input.setDescription(request.getDescription());
        input.setIsMandatory(request.getIsMandatory());
        return input;
    }

    @Override
    public InputResponseDto toDto(Input input) {
        return new InputResponseDto(
                input.getId(),
                input.getName(),
                input.getDescription(),
                input.getIsMandatory(),
                input.getIsTrigger(),
                input.getProcessSchemaId(),
                input.getInputTypeId()
        );
    }

}