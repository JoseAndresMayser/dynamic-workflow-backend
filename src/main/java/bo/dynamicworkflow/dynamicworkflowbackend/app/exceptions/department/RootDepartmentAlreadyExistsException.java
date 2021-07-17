package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class RootDepartmentAlreadyExistsException extends DepartmentException {

    public RootDepartmentAlreadyExistsException() {
        this("Ya existe un departamento raíz");
    }

    public RootDepartmentAlreadyExistsException(String message) {
        super(message);
    }

}