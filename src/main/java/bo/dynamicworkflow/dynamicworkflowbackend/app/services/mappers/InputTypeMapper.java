package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.InputType;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.InputTypeDto;

public class InputTypeMapper extends BaseMapper<InputTypeDto, InputTypeDto, InputType> {

    @Override
    public InputType toEntity(InputTypeDto request) {
        throw new UnsupportedOperationException("Function not supported.");
    }

    @Override
    public InputTypeDto toDto(InputType inputType) {
        InputTypeDto inputTypeDto = new InputTypeDto();
        inputTypeDto.setId(inputType.getId());
        inputTypeDto.setName(inputType.getName());
        inputTypeDto.setDescription(inputType.getDescription());
        return inputTypeDto;
    }

}