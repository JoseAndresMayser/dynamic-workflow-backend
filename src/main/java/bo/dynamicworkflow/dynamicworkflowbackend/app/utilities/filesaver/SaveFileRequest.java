package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveFileRequest {

    private String label;
    private String fileContent;
    private String extension;

}