package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process;

public class InactiveProcessException extends ProcessException {

    public InactiveProcessException() {
        this("El Proceso se encuentra INACTIVO.");
    }

    public InactiveProcessException(String message) {
        super(message);
    }

}