package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restrictions")
@Data
public class Restriction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "has_defined_values", nullable = false)
    private Boolean hasDefinedValues;

    @Column(name = "input_type_id", nullable = false)
    private Integer inputTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private InputType inputType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restriction")
    private List<RestrictionDefinedValue> restrictionDefinedValues;

}