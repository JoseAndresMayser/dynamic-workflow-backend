package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class DepartmentNotFoundException extends DepartmentException {

    public DepartmentNotFoundException() {
        this("Departamento no encontrado");
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }

}