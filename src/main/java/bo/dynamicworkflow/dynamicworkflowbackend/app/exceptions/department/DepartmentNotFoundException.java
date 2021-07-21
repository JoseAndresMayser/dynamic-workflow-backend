package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class DepartmentNotFoundException extends DepartmentException {

    public DepartmentNotFoundException() {
        this("Departamento no encontrado.");
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }

    public DepartmentNotFoundException(Integer departmentId) {
        super(String.format("No se pudo encontrar el Departamento con Id: %d.", departmentId));
    }

}