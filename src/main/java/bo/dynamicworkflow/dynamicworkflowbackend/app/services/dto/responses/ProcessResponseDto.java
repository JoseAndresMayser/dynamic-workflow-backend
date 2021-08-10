package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessResponseDto {

    private Integer id;
    private String name;
    private String description;
    private Timestamp creationTimestamp;
    private Timestamp modificationTimestamp;
    private ProcessStatus status;
    private Integer userId;
    private Integer departmentId;

}