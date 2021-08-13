package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input;

public class InputNotFoundException extends InputException {

    public InputNotFoundException() {
        this("Entrada del formulario no encontrada.");
    }

    public InputNotFoundException(String message) {
        super(message);
    }

    public InputNotFoundException(Integer inputId) {
        super(String.format("No se pudo encontrar la Entrada del formulario con Id: %d.", inputId));
    }

}