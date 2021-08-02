package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ProcessStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProcessRequestDto {

    private String name;
    private String description;
    private ProcessStatus status;
    private Integer departmentId;
    private Timestamp startTimestamp;
    private Timestamp finishTimestamp;

}