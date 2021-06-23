package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_actions")
@Data
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "action_id", nullable = false)
    private Integer actionId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "action_id", insertable = false, updatable = false)
    private Action action;

}