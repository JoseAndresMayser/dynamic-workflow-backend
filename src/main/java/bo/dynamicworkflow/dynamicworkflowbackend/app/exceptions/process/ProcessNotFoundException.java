package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process;

public class ProcessNotFoundException extends ProcessException {

    public ProcessNotFoundException() {
        this("Proceso no encontrado.");
    }

    public ProcessNotFoundException(String message) {
        super(message);
    }

    public ProcessNotFoundException(Integer processId) {
        super(String.format("No se pudo encontrar el Proceso con Id: %d.", processId));
    }

}