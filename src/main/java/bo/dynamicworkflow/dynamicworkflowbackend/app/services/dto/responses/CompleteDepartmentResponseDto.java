package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteDepartmentResponseDto {

    private DepartmentResponseDto department;
    private UserResponseDto departmentBoss;
    private List<UserResponseDto> analystMembers;

}