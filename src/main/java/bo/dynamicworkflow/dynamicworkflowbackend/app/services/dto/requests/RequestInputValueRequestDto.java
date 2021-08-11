package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class RequestInputValueRequestDto {

    private Integer inputId;
    private Object value;

}