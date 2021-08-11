package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponseDto {

    private Integer id;
    private Timestamp shippingTimestamp;
    private Timestamp finishTimestamp;
    private RequestStatus status;
    private String code;
    private Integer processId;
    private Integer userId;

}