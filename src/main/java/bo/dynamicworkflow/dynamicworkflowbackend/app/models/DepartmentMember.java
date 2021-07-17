package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "department_members")
@Data
public class DepartmentMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_department_boss", nullable = false)
    private Boolean isDepartmentBoss;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Department department;

}