package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "request_input_values")
@Data
public class RequestInputValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", columnDefinition = "Text", nullable = false)
    private String value;

    @Column(name = "input_id", nullable = false)
    private Integer inputId;

    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Input input;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Request request;

}