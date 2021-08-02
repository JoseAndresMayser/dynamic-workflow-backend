package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process;

public class ProcessAlreadyExistsException extends ProcessException {

    public ProcessAlreadyExistsException() {
        this("El proceso ya se encuentra registrado.");
    }

    public ProcessAlreadyExistsException(String message) {
        super(message);
    }

}