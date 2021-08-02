package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action;

public class InvalidActionIdListException extends ActionException {

    public InvalidActionIdListException() {
        this("La lista de ID de acciones es inválida.");
    }

    public InvalidActionIdListException(String message) {
        super(message);
    }

}