package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "restriction_defined_values")
@Data
public class RestrictionDefinedValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", nullable = false, length = 128)
    private String value;

    @Column(name = "restriction_id", nullable = false)
    private Integer restrictionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restriction_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Restriction restriction;

}