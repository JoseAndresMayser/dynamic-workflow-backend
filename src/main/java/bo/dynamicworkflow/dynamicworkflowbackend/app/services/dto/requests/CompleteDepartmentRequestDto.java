package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class CompleteDepartmentRequestDto {

    private DepartmentRequestDto department;
    private Integer departmentBossId;
    private List<Integer> analystMembersId;

}