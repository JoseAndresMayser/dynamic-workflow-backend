package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action;

public class ActionWithoutAuthException extends ActionException {

    public ActionWithoutAuthException() {
        this("Acción sin autenticación");
    }

    public ActionWithoutAuthException(String message) {
        super(message);
    }

}