package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ExecutedAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestActionResponseDto {

    private Integer id;
    private Timestamp executionTimestamp;
    private ExecutedAction executedAction;
    private String commentary;
    private Integer requestId;
    private Integer stageAnalystId;

}