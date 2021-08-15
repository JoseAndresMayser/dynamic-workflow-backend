package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request;

public class InvalidRequestStatusException extends RequestException {

    public InvalidRequestStatusException() {
        this("El estado de la solicitud es inválido.");
    }

    public InvalidRequestStatusException(String message) {
        super(message);
    }

}