package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.InputTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputTypeDto {

    private Integer id;
    private InputTypeName name;
    private String description;
    private List<RestrictionDto> restrictions;

}