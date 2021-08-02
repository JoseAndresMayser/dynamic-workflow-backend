package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction;

public class InvalidRestrictionValueException extends RestrictionException {

    public InvalidRestrictionValueException() {
        this("El valor de la restricción es inválido.");
    }

    public InvalidRestrictionValueException(String message) {
        super(message);
    }

}