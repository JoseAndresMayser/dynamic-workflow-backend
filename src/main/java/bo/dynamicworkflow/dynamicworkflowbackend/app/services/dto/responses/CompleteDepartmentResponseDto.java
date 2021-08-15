package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.DepartmentMemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteDepartmentResponseDto {

    private DepartmentResponseDto department;
    private DepartmentMemberDto departmentBoss;
    private List<DepartmentMemberDto> analystMembers;

}