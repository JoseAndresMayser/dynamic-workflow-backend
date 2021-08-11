package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputRestrictionResponseDto {

    private Integer id;
    private String value;
    private Integer inputId;
    private Integer restrictionId;

}