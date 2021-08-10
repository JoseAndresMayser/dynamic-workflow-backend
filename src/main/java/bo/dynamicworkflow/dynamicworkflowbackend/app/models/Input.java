package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "inputs")
@Data
public class Input {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory;

    @Column(name = "is_trigger", nullable = false)
    private Boolean isTrigger;

    @Column(name = "process_schema_id", nullable = false)
    private Integer processSchemaId;

    @Column(name = "input_type_id", nullable = false)
    private Integer inputTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_schema_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProcessSchema processSchema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private InputType inputType;

}