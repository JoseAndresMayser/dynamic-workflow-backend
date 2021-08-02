package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "stage_analysts")
@Data
public class StageAnalyst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "requires_approval", nullable = false)
    private Boolean requiresApproval;

    @Column(name = "approval_is_mandatory", nullable = false)
    private Boolean approvalIsMandatory;

    @Column(name = "stage_id", nullable = false)
    private Integer stageId;

    @Column(name = "department_member_id", nullable = false)
    private Integer departmentMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Stage stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentMember departmentMember;

}