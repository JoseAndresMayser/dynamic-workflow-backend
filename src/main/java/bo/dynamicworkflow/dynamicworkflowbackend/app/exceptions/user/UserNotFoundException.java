package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        this("Usuario no encontrado.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}