package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input;

public class InvalidInputValueException extends InputException {

    public InvalidInputValueException() {
        this("El valor de la Entrada del formulario es inválido.");
    }

    public InvalidInputValueException(String message) {
        super(message);
    }

}