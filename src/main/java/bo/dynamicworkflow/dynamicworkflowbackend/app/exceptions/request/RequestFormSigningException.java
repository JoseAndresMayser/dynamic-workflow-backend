package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request;

public class RequestFormSigningException extends RequestException {

    public RequestFormSigningException(String message) {
        super(message);
    }

    public RequestFormSigningException(String message, Throwable cause) {
        super(message, cause);
    }

}