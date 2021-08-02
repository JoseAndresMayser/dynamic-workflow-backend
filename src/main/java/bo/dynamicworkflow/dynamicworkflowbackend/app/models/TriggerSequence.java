package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "trigger_sequences")
@Data
public class TriggerSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "has_next_stage", nullable = false)
    private Boolean hasNextStage;

    @Column(name = "current_stage_index", nullable = false)
    private Byte currentStageIndex;

    @Column(name = "next_stage_index")
    private Byte nextStageIndex;

    @Column(name = "selection_input_value_id", nullable = false)
    private Integer selectionInputValueId;

    @Column(name = "current_stage_id", nullable = false)
    private Integer currentStageId;

    @Column(name = "next_stage_id")
    private Integer nextStageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selection_input_value_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SelectionInputValue selectionInputValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_stage_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Stage currentStage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_stage_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Stage nextStage;

}