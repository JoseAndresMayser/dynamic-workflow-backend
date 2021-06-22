package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class UserRequestDto {

    private String username;
    private String password;
    private String passwordConfirmation;
    private String status;
    private String names;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String phoneNumber;
    private String code;

}