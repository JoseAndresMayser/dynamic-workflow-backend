package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class SelectionInputValueRequestDto {

    private String value;
    private List<TriggerSequenceRequestDto> triggerSequences;

}