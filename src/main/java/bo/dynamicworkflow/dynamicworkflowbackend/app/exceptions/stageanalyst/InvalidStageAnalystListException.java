package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst;

public class InvalidStageAnalystListException extends StageAnalystException {

    public InvalidStageAnalystListException() {
        this("La lista de analistas de la etapa es inválida.");
    }

    public InvalidStageAnalystListException(String message) {
        super(message);
    }

}