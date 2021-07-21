package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class CompleteRoleRequestDto {

    private RoleRequestDto role;
    private List<Integer> actionsId;

}