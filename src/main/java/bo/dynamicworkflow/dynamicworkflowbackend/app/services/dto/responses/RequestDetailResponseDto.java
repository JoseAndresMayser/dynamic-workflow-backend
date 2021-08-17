package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailResponseDto {

    private RequestResponseDto request;
    private ProcessResponseDto process;
    private UserResponseDto user;

}