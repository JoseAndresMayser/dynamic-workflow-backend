package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "selection_input_values")
@Data
public class SelectionInputValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", nullable = false, length = 128)
    private String value;

    @Column(name = "input_id", nullable = false)
    private Integer inputId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Input input;

}