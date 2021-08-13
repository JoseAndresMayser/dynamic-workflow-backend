package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class NewRequestRequestDto {

    private Integer processId;
    List<RequestInputValueRequestDto> inputValues;

}