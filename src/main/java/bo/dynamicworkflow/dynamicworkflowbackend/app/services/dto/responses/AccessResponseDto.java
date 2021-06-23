package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.UserStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.ActionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessResponseDto {

    private Integer userId;
    private String username;
    private UserStatus userStatus;
    private String userFullName;
    private String userEmail;
    private String userPhoneNumber;
    private String userCode;
    private String token;
    private List<ActionDto> userActions;

}