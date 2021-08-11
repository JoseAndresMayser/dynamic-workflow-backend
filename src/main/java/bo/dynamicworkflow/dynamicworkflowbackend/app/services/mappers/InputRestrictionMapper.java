package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.InputRestriction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.InputRestrictionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.InputRestrictionResponseDto;

public class InputRestrictionMapper extends
        BaseMapper<InputRestrictionRequestDto, InputRestrictionResponseDto, InputRestriction> {

    @Override
    public InputRestriction toEntity(InputRestrictionRequestDto request) {
        InputRestriction inputRestriction = new InputRestriction();
        inputRestriction.setValue(request.getValue());
        inputRestriction.setRestrictionId(request.getRestrictionId());
        return inputRestriction;
    }

    @Override
    public InputRestrictionResponseDto toDto(InputRestriction inputRestriction) {
        return new InputRestrictionResponseDto(
                inputRestriction.getId(),
                inputRestriction.getValue(),
                inputRestriction.getInputId(),
                inputRestriction.getRestrictionId()
        );
    }

}