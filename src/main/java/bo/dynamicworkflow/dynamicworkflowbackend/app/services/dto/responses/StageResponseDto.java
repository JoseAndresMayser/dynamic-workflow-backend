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
    private Byte approvalsRequired;
    private Boolean hasConditional;
    private Byte stageIndex;
    private Byte previousStageIndex;
    private Byte nextStageIndex;
    private Integer previousStageId;
    private Integer nextStageId;
    private Integer processSchemaId;
    private StageResponseDto previousStage;
    private StageResponseDto nextStage;

}