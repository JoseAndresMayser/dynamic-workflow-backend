package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class CompleteUserRequestDto {

    private UserRequestDto user;
    private List<Integer> actionsId;

}