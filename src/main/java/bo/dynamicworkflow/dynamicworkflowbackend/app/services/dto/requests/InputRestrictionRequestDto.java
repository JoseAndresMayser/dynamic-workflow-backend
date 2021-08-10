package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class InputRestrictionRequestDto {

    private String value;
    private Integer restrictionId;

}