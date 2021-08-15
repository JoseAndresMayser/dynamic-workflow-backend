package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst;

public class InvalidStageAnalystException extends StageAnalystException {

    public InvalidStageAnalystException() {
        this("Analista de etapa inválido.");
    }

    public InvalidStageAnalystException(String message) {
        super(message);
    }

}