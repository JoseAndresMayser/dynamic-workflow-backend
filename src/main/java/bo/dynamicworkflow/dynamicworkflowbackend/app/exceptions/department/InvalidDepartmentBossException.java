package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class InvalidDepartmentBossException extends DepartmentException {

    public InvalidDepartmentBossException() {
        this("Jefe de departamento no válido.");
    }

    public InvalidDepartmentBossException(String message) {
        super(message);
    }

}