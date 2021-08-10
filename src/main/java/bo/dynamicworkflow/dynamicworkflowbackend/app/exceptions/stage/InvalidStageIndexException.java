package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage;

public class InvalidStageIndexException extends StageException {

    public InvalidStageIndexException() {
        this("El índice de la etapa es inválido.");
    }

    public InvalidStageIndexException(String message) {
        super(message);
    }

}