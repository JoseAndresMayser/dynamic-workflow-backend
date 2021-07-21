package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class DepartmentStatusException extends DepartmentException {

    public DepartmentStatusException() {
        this("Estado de departamento no válido");
    }

    public DepartmentStatusException(String message) {
        super(message);
    }

}