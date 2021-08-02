package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage;

public class InvalidStageNameException extends StageException {

    public InvalidStageNameException() {
        this("El nombre de la etapa es inválido.");
    }

    public InvalidStageNameException(String message) {
        super(message);
    }

}