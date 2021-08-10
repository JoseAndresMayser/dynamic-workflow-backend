package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.selectioninputvalue;

public class InvalidSelectionValueException extends SelectionInputValueException {

    public InvalidSelectionValueException() {
        this("El valor de selección es inválido.");
    }

    public InvalidSelectionValueException(String message) {
        super(message);
    }

}