package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException() {
        this("Contraseña incorrecta.");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

}