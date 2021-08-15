package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "stages")
@Data
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "approvals_required", nullable = false)
    private Integer approvalsRequired;

    @Column(name = "has_conditional", nullable = false)
    private Boolean hasConditional;

    @Column(name = "stage_index", nullable = false)
    private Integer stageIndex;

    @Column(name = "previous_stage_index")
    private Integer previousStageIndex;

    @Column(name = "next_stage_index")
    private Integer nextStageIndex;

    @Column(name = "previous_stage_id")
    private Integer previousStageId;

    @Column(name = "next_stage_id")
    private Integer nextStageId;

    @Column(name = "process_schema_id", nullable = false)
    private Integer processSchemaId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_stage_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Stage previousStage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_stage_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Stage nextStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_schema_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProcessSchema processSchema;

}