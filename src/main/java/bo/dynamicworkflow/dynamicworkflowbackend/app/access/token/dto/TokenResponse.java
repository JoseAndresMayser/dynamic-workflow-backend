package bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    private String username;
    private String token;
    private String tokenType;
    private Date expirationDate;

}