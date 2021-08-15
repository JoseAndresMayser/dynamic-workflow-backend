package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request;

public class RequestNotFoundException extends RequestException {

    public RequestNotFoundException() {
        this("Solicitud no encontrada.");
    }

    public RequestNotFoundException(String message) {
        super(message);
    }

    public RequestNotFoundException(Integer requestId) {
        super(String.format("No se pudo encontrar la Solicitud con Id: %d.", requestId));
    }

}