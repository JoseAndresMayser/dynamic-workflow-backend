package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action;

public class ActionNotFoundException extends ActionException {

    public ActionNotFoundException() {
        this("Acción no encontrada");
    }

    public ActionNotFoundException(String message) {
        super(message);
    }

}