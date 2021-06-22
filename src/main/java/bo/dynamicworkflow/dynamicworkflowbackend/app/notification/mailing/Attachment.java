package bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    private String name;
    private String extension;
    private byte[] content;

}