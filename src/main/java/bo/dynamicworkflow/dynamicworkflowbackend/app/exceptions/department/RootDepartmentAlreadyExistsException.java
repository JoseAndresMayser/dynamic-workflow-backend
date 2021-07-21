package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;

public class RootDepartmentAlreadyExistsException extends DepartmentException {

    public RootDepartmentAlreadyExistsException() {
        this("Ya existe un Departamento raíz. Debe seleccionar un Departamento Padre.");
    }

    public RootDepartmentAlreadyExistsException(String message) {
        super(message);
    }

}