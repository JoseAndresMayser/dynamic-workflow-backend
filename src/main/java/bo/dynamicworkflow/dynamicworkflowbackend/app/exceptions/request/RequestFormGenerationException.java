package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request;

public class RequestFormGenerationException extends RequestException {

    public RequestFormGenerationException(String message) {
        super(message);
    }

    public RequestFormGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

}