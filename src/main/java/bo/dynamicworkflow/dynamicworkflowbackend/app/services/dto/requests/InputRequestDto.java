package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class InputRequestDto {

    private String name;
    private String description;
    private Boolean isMandatory;
    private Integer inputTypeId;
    private List<InputRestrictionRequestDto> restrictions;
    private List<SelectionInputValueRequestDto> selectionValues;

}