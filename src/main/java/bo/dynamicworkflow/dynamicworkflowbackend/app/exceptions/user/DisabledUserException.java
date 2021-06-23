package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user;

public class DisabledUserException extends UserException {

    public DisabledUserException() {
        this("El Usuario se encuentra deshabilitado.");
    }

    public DisabledUserException(String message) {
        super(message);
    }

}