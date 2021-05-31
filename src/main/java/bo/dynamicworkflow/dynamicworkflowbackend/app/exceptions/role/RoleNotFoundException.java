package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role;

public class RoleNotFoundException extends RoleException {

    public RoleNotFoundException() {
        this("Rol no encontrado");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }

}