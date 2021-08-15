package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ExecutedAction;
import lombok.Data;

@Data
public class RequestActionRequestDto {

    private ExecutedAction executedAction;
    private String commentary;
    private String digitalCertificatePassword;

}