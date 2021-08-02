package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input;

public class InvalidInputListException extends InputException {

    public InvalidInputListException() {
        this("La lista de campos del formulario es inválida.");
    }

    public InvalidInputListException(String message) {
        super(message);
    }

}