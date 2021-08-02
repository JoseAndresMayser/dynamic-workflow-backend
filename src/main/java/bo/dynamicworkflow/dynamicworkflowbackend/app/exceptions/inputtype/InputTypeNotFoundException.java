package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.inputtype;

public class InputTypeNotFoundException extends InputTypeException {

    public InputTypeNotFoundException() {
        this("Tipo de campo no encontrado.");
    }

    public InputTypeNotFoundException(String message) {
        super(message);
    }

    public InputTypeNotFoundException(Integer inputTypeId) {
        super(String.format("No se pudo encontrar un tipo de campo con Id: %d.", inputTypeId));
    }

}