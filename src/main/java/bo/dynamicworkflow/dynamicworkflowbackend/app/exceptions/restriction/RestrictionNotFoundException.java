package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction;

public class RestrictionNotFoundException extends RestrictionException {

    public RestrictionNotFoundException() {
        this("Restricción no encontrada.");
    }

    public RestrictionNotFoundException(String message) {
        super(message);
    }

    public RestrictionNotFoundException(Integer restrictionId) {
        this(String.format("No se pudo encontrar la restricción con Id: %d.", restrictionId));
    }

}