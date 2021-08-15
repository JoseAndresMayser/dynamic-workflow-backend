package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.requeststage;

public class FinishedRequestStageException extends RequestStageException {

    public FinishedRequestStageException() {
        this("La etapa en turno de la solicitud ya se encuentra finalizada.");
    }

    public FinishedRequestStageException(String message) {
        super(message);
    }

}