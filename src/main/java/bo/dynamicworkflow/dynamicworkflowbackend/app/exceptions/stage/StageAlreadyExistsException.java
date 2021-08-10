package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage;

public class StageAlreadyExistsException extends StageException {

    public StageAlreadyExistsException() {
        this("La etapa ya se encuentra registrada.");
    }

    public StageAlreadyExistsException(String message) {
        super(message);
    }

}