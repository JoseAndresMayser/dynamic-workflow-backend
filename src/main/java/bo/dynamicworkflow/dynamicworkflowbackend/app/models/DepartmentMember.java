package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "department_members")
@Data
public class DepartmentMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_department_boss", nullable = false)
    private Boolean isDepartmentBoss;

    @Column(name = "assignment_timestamp", nullable = false)
    private Timestamp assignmentTimestamp;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;

}