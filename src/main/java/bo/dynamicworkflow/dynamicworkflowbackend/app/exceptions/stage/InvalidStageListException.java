package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage;

public class InvalidStageListException extends StageException {

    public InvalidStageListException() {
        this("La lista de etapas es inválida.");
    }

    public InvalidStageListException(String message) {
        super(message);
    }

}