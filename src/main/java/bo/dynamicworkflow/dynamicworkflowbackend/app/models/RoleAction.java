package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "role_actions")
@Data
public class RoleAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "action_id", nullable = false)
    private Integer actionId;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "action_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Action action;

}