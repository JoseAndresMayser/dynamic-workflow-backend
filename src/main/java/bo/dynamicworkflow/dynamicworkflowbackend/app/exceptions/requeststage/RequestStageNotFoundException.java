package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.requeststage;

public class RequestStageNotFoundException extends RequestStageException {

    public RequestStageNotFoundException() {
        this("No se pudo encontrar la etapa en la que se encuentra la solicitud.");
    }

    public RequestStageNotFoundException(String message) {
        super(message);
    }

}