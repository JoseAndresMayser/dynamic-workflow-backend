package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "input_restrictions")
@Data
public class InputRestriction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", nullable = false, length = 256)
    private String value;

    @Column(name = "input_id", nullable = false)
    private Integer inputId;

    @Column(name = "restriction_id", nullable = false)
    private Integer restrictionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Input input;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restriction_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Restriction restriction;

}