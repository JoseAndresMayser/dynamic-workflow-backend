package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ExecutedAction;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "request_actions")
@Data
public class RequestAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "execution_timestamp", nullable = false)
    private Timestamp executionTimestamp;

    @Column(name = "executed_action", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ExecutedAction executedAction;

    @Column(name = "commentary", length = 256)
    private String commentary;

    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "stage_analyst_id", nullable = false)
    private Integer stageAnalystId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Request request;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_analyst_id", referencedColumnName = "id", insertable = false, updatable = false)
    private StageAnalyst stageAnalyst;

}