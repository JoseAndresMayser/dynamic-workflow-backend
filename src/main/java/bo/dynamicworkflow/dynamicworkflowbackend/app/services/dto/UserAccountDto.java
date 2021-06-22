package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {

    private Integer id;
    private String username;
    private String names;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String code;

}