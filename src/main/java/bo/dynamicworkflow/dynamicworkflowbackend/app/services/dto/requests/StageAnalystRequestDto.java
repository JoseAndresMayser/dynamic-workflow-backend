package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class StageAnalystRequestDto {

    private Boolean requiresApproval;
    private Boolean approvalIsMandatory;
    private Integer departmentMemberId;

}