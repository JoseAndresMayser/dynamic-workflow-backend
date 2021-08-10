package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto;

import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DepartmentResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentMemberDto {

    private Integer id;
    private Boolean isDepartmentBoss;
    private Timestamp assignmentTimestamp;
    private Boolean isActive;
    private Integer userId;
    private Integer departmentId;
    private UserResponseDto user;
    private DepartmentResponseDto department;

}