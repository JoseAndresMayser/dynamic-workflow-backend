package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role;

public class UpdateMainRoleException extends RoleException {

    public UpdateMainRoleException() {
        this("No se puede modificar un Rol principal.");
    }

    public UpdateMainRoleException(String message) {
        super(message);
    }

}