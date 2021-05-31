package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role;

public class RoleAlreadyExistsException extends RoleException {

    public RoleAlreadyExistsException() {
        this("El rol ya se encuentra registrado");
    }

    public RoleAlreadyExistsException(String message) {
        super(message);
    }

}