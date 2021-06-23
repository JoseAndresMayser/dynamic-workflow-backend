package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user;

public class UserStatusException extends UserException {

    public UserStatusException() {
        this("Estado de usuario no válido.");
    }

    public UserStatusException(String message) {
        super(message);
    }

}