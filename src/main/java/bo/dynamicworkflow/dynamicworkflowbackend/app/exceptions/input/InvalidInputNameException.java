package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input;

public class InvalidInputNameException extends InputException {

    public InvalidInputNameException() {
        this("El nombre del campo del formulario es inválido.");
    }

    public InvalidInputNameException(String message) {
        super(message);
    }

}