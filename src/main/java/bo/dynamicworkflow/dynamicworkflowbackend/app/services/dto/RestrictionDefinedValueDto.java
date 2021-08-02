package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestrictionDefinedValueDto {

    private Integer id;
    private String value;
    private Integer restrictionId;

}