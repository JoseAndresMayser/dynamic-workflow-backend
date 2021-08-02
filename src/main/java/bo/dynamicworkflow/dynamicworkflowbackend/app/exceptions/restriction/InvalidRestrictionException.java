package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction;

public class InvalidRestrictionException extends RestrictionException {

    public InvalidRestrictionException() {
        this("La restricción es inválida.");
    }

    public InvalidRestrictionException(String message) {
        super(message);
    }

}