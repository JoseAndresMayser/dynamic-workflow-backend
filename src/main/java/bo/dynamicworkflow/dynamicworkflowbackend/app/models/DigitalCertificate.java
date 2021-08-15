package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "digital_certificates")
@Data
public class DigitalCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "path", nullable = false, length = 512)
    private String path;

    @Column(name = "upload_timestamp", nullable = false)
    private Timestamp uploadTimestamp;

    @Column(name = "modification_timestamp", nullable = false)
    private Timestamp modificationTimestamp;

    @Column(name = "department_member_id", nullable = false)
    private Integer departmentMemberId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentMember departmentMember;

}