package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.SelectionInputValue;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.SelectionInputValueRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.SelectionInputValueResponseDto;

public class SelectionInputValueMapper extends
        BaseMapper<SelectionInputValueRequestDto, SelectionInputValueResponseDto, SelectionInputValue> {

    @Override
    public SelectionInputValue toEntity(SelectionInputValueRequestDto request) {
        SelectionInputValue selectionInputValue = new SelectionInputValue();
        selectionInputValue.setValue(request.getValue());
        return selectionInputValue;
    }

    @Override
    public SelectionInputValueResponseDto toDto(SelectionInputValue selectionInputValue) {
        return new SelectionInputValueResponseDto(
                selectionInputValue.getId(),
                selectionInputValue.getValue(),
                selectionInputValue.getInputId()
        );
    }

}