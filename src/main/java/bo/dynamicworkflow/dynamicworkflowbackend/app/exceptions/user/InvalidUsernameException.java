package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user;

public class InvalidUsernameException extends UserException {

    public InvalidUsernameException() {
        this("Nombre de usuario inválido.");
    }

    public InvalidUsernameException(String message) {
        super(message);
    }

}