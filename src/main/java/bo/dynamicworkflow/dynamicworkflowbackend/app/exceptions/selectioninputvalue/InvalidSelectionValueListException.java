package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.selectioninputvalue;

public class InvalidSelectionValueListException extends SelectionInputValueException {

    public InvalidSelectionValueListException() {
        this("La lista de valores de selección es inválida.");
    }

    public InvalidSelectionValueListException(String message) {
        super(message);
    }

}