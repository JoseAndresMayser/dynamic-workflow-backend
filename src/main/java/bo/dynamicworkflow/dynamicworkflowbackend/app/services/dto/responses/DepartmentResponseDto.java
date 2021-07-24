package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.DepartmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDto {

    private Integer id;
    private String name;
    private String contactEmail;
    private String contactPhone;
    private String location;
    private Timestamp creationTimestamp;
    private Timestamp modificationTimestamp;
    private DepartmentStatus status;
    private Integer parentDepartmentId;
    private DepartmentResponseDto parentDepartment;
    private List<DepartmentResponseDto> subordinateDepartments;

}