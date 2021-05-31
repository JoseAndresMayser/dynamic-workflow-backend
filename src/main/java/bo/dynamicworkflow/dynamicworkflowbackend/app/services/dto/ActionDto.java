package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDto {

    private Integer id;
    private ActionCode code;
    private String description;

}