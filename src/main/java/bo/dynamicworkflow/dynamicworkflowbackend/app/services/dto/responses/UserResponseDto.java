package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Integer id;
    private String username;
    private UserStatus status;
    private Timestamp creationDate;
    private Timestamp lastModifiedDate;
    private String names;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String phoneNumber;
    private String code;

}