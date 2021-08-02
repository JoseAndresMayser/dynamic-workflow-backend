package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputResponseDto {

    private Integer id;
    private String name;
    private String description;
    private Boolean isMandatory;
    private Boolean isTrigger;
    private Integer processSchemaId;
    private Integer inputTypeId;

}