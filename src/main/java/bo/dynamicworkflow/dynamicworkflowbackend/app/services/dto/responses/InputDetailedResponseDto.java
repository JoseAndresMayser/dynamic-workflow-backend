package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.InputTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDetailedResponseDto {

    private InputResponseDto input;
    private InputTypeDto inputType;
    private List<InputRestrictionResponseDto> restrictions;
    private List<SelectionInputValueResponseDto> selectionInputValues;

}