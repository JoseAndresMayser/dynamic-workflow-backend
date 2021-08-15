package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRequestDto {

    private String fileContent;
    private String extension;

}