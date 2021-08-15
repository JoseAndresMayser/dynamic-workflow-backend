package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request;

public class InvalidCommentaryException extends RequestException {

    public InvalidCommentaryException() {
        this("Comantario para la acción de la solicitud inválido.");
    }

    public InvalidCommentaryException(String message) {
        super(message);
    }

}