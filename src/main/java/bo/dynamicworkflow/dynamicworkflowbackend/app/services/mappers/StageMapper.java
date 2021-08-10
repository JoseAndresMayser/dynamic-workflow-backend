package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Stage;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.StageRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.StageResponseDto;

public class StageMapper extends BaseMapper<StageRequestDto, StageResponseDto, Stage> {

    @Override
    public Stage toEntity(StageRequestDto request) {
        Stage stage = new Stage();
        stage.setName(request.getName());
        stage.setDescription(request.getDescription());
        stage.setApprovalsRequired(request.getApprovalsRequired());
        stage.setStageIndex(request.getStageIndex());
        stage.setPreviousStageIndex(request.getPreviousStageIndex());
        stage.setNextStageIndex(request.getNextStageIndex());
        return stage;
    }

    @Override
    public StageResponseDto toDto(Stage stage) {
        return stage == null ? null :
                new StageResponseDto(
                        stage.getId(),
                        stage.getName(),
                        stage.getDescription(),
                        stage.getApprovalsRequired(),
                        stage.getHasConditional(),
                        stage.getStageIndex(),
                        stage.getPreviousStageIndex(),
                        stage.getNextStageIndex(),
                        stage.getPreviousStageId(),
                        stage.getNextStageId(),
                        stage.getProcessSchemaId(),
                        toDto(stage.getPreviousStage()),
                        toDto(stage.getNextStage())
                );
    }

}