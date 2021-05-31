package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "actions")
@Data
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false, length = 256)
    @Enumerated(EnumType.STRING)
    private ActionCode code;

    @Column(name = "description", nullable = false, length = 256)
    private String description;

}