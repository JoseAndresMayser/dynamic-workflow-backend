package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request;

public class ActionOnRequestAlreadyExecutedException extends RequestException {

    public ActionOnRequestAlreadyExecutedException() {
        this("Ya ha sido ejecutada por el Analista actual una acción sobre la solicitud.");
    }

    public ActionOnRequestAlreadyExecutedException(String message) {
        super(message);
    }

}