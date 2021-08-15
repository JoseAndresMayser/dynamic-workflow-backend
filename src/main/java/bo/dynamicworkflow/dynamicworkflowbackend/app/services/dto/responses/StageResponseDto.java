package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StageResponseDto {

    private Integer id;
    private String name;
    private String description;
    private Integer approvalsRequired;
    private Boolean hasConditional;
    private Integer stageIndex;
    private Integer previousStageIndex;
    private Integer nextStageIndex;
    private Integer previousStageId;
    private Integer nextStageId;
    private Integer processSchemaId;
    private StageResponseDto previousStage;
    private StageResponseDto nextStage;

}