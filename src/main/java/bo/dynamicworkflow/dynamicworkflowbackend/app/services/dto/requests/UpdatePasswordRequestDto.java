package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class UpdatePasswordRequestDto {

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirmation;

}