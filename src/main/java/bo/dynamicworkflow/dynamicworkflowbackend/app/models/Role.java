package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 256)
    private String description;

    @PrePersist
    @PreUpdate
    private void prePersistOrUpdate() {
        name = name.toUpperCase();
    }

}