package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class StageRequestDto {

    private String name;
    private String description;
    private Byte approvalsRequired;
    private Byte stageIndex;
    private Byte previousStageIndex;
    private Byte nextStageIndex;
    private List<StageAnalystRequestDto> analysts;

}