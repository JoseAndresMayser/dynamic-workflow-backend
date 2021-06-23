package bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.dto;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.enums.TimeInstanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest<Account> {

    private String username;
    private TimeInstanceType timeInstanceType;
    private Integer TokenAvailabilityTime;
    private Account account;

}