package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDetailResponseDto {

    private ProcessResponseDto process;
    private UserResponseDto user;
    private DepartmentResponseDto department;

}