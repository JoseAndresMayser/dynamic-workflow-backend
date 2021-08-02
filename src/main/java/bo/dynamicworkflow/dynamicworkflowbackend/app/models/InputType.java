package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.InputTypeName;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "input_types")
@Data
public class InputType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private InputTypeName name;

    @Column(name = "description", nullable = false, length = 256)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inputType")
    private List<Restriction> restrictions;

}