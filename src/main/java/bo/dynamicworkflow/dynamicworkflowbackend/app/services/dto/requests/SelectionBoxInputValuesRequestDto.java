package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class SelectionBoxInputValuesRequestDto {

    private List<String> inputValues;

}