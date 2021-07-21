package bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests;

import lombok.Data;

@Data
public class DepartmentRequestDto {

    private String name;
    private String contactEmail;
    private String contactPhone;
    private String location;
    private String status;
    private Integer parentDepartmentId;

}