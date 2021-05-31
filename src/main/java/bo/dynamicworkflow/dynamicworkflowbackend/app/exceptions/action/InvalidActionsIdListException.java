package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action;

public class InvalidActionsIdListException extends ActionException {

    public InvalidActionsIdListException() {
        this("La lista de ID de acciones es inválida.");
    }

    public InvalidActionsIdListException(String message) {
        super(message);
    }

}