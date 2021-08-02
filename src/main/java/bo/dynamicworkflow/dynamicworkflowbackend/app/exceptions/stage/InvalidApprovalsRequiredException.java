package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage;

public class InvalidApprovalsRequiredException extends StageException {

    public InvalidApprovalsRequiredException() {
        this("La cantidad de aprobaciones requeridas para la etapa es inválida.");
    }

    public InvalidApprovalsRequiredException(String message) {
        super(message);
    }

}