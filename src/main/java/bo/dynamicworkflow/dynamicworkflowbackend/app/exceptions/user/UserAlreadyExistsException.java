package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException() {
        this("El Usuario ya se encuentra registrado.");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}