package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.DepartmentStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "contact_email", nullable = false, length = 320)
    private String contactEmail;

    @Column(name = "contact_phone", nullable = false, length = 50)
    private String contactPhone;

    @Column(name = "location", nullable = false, length = 256)
    private String location;

    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @Column(name = "last_modified_date", nullable = false)
    private Timestamp lastModifiedDate;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private DepartmentStatus status;

    @Column(name = "parent_department_id")
    private Integer parentDepartmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Department parentDepartment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentDepartment")
    private List<Department> subordinateDepartments;

}