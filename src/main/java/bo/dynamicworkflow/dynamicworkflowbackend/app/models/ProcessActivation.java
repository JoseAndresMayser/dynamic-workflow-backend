package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "process_activations")
@Data
public class ProcessActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_indeterminate", nullable = false)
    private Boolean isIndeterminate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "start_timestamp")
    private Timestamp startTimestamp;

    @Column(name = "finish_timestamp")
    private Timestamp finishTimestamp;

    @Column(name = "process_schema_id", nullable = false)
    private Integer processSchemaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_schema_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProcessSchema processSchema;

}