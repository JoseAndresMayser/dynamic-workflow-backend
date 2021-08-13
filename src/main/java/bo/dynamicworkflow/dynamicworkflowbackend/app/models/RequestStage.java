package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.RequestStageStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "request_stages")
@Data
public class RequestStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "entry_timestamp", nullable = false)
    private Timestamp entryTimestamp;

    @Column(name = "finish_timestamp")
    private Timestamp finishTimestamp;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private RequestStageStatus status;

    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "stage_id", nullable = false)
    private Integer stageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Stage stage;

}