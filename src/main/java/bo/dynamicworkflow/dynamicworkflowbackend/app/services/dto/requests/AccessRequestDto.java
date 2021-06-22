package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class AccessRequestDto {

    private String username;
    private String password;

}