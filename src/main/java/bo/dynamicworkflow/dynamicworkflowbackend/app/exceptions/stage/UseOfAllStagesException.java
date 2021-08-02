package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage;

public class UseOfAllStagesException extends StageException {

    public UseOfAllStagesException() {
        this("No se están utilizando todas las etapas en el proceso.");
    }

    public UseOfAllStagesException(String message) {
        super(message);
    }

}