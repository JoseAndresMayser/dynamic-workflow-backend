package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class StageRequestDto {

    private String name;
    private String description;
    private Integer approvalsRequired;
    private Integer stageIndex;
    private Integer previousStageIndex;
    private Integer nextStageIndex;
    private List<StageAnalystRequestDto> analysts;

}