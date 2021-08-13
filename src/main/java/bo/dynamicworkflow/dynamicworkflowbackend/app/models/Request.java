package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.RequestStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "requests")
@Data
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "shipping_timestamp", nullable = false)
    private Timestamp shippingTimestamp;

    @Column(name = "finish_timestamp")
    private Timestamp finishTimestamp;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "form_path", length = 512)
    private String formPath;

    @Column(name = "process_id", nullable = false)
    private Integer processId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Process process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

}