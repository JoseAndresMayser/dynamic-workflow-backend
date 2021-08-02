package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input;

public class InputAlreadyExistsException extends InputException {

    public InputAlreadyExistsException() {
        this("El campo del formulario ya se encuentra registrado.");
    }

    public InputAlreadyExistsException(String message) {
        super(message);
    }

}