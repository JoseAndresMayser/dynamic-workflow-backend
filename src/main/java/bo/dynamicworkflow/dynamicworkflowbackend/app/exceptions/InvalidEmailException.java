package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions;

public class InvalidEmailException extends Exception {

    public InvalidEmailException() {
        this("Correo electrónico inválido.");
    }

    public InvalidEmailException(String message) {
        super(message);
    }

}