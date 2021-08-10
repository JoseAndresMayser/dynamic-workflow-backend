package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class DisabledDepartmentException extends DepartmentException {

    public DisabledDepartmentException() {
        this("El departamento se encuentra en estado DESACTIVADO.");
    }

    public DisabledDepartmentException(String message) {
        super(message);
    }

}