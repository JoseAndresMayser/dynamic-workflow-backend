package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestrictionDto {

    private Integer id;
    private String name;
    private Boolean hasDefinedValues;
    private Integer inputTypeId;
    private List<RestrictionDefinedValueDto> definedValues;

}