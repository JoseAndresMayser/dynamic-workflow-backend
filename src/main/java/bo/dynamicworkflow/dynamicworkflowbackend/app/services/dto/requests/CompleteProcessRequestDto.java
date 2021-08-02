package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class CompleteProcessRequestDto {

    private ProcessRequestDto process;
    private List<InputRequestDto> inputs;
    private List<StageRequestDto> stages;

}