package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class TriggerSequenceRequestDto {

    private Integer currentStageIndex;
    private Integer nextStageIndex;

}