package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class InvalidDepartmentNameException extends DepartmentException {

    public InvalidDepartmentNameException() {
        this("El nombre del departamento es inválido.");
    }

    public InvalidDepartmentNameException(String message) {
        super(message);
    }

}