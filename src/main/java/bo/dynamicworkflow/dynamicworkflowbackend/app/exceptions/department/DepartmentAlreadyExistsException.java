package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class DepartmentAlreadyExistsException extends DepartmentException {

    public DepartmentAlreadyExistsException() {
        this("El departamento ya se encuentra registrado");
    }

    public DepartmentAlreadyExistsException(String message) {
        super(message);
    }

}