package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalCertificateResponseDto {

    private Integer id;
    private String path;
    private Timestamp uploadTimestamp;
    private Timestamp modificationTimestamp;
    private Integer departmentMemberId;

}