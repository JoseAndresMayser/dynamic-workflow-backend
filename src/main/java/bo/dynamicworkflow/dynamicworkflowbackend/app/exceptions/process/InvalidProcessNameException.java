package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process;

public class InvalidProcessNameException extends ProcessException {

    public InvalidProcessNameException() {
        this("El nombre del proceso es inválido.");
    }

    public InvalidProcessNameException(String message) {
        super(message);
    }

}