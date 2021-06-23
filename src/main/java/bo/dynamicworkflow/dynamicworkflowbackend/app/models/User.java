package bo.dynamicworkflow.dynamicworkflowbackend.app.models;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.UserStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @Column(name = "last_modified_date", nullable = false)
    private Timestamp lastModifiedDate;

    @Column(name = "names", nullable = false, length = 256)
    private String names;

    @Column(name = "first_surname", nullable = false, length = 256)
    private String firstSurname;

    @Column(name = "second_surname", length = 256)
    private String secondSurname;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "code", length = 50)
    private String code;

    @PrePersist
    @PreUpdate
    private void prePersistOrUpdate() {
        names = names.toUpperCase();
        firstSurname = firstSurname.toUpperCase();
        if (secondSurname != null && secondSurname.equals("")) {
            secondSurname = null;
        }
        if (secondSurname != null) {
            secondSurname = secondSurname.toUpperCase();
        }
        email = email.toLowerCase();
    }

    public String fullName() {
        StringBuilder fullName = new StringBuilder(names);
        if (firstSurname != null) {
            fullName.append(" ").append(firstSurname);
        }
        if (secondSurname != null) {
            fullName.append(" ").append(secondSurname);
        }
        return fullName.toString();
    }

}