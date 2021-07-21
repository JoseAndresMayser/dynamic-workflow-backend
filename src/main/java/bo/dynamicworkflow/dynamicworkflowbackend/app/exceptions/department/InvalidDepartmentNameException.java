package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class InvalidDepartmentNameException extends DepartmentException {

    public InvalidDepartmentNameException() {
        this("Nombre de departamento inválido");
    }

    public InvalidDepartmentNameException(String message) {
        super(message);
    }

}