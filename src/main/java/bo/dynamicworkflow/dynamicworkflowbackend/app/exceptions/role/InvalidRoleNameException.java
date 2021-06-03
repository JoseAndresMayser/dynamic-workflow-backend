package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role;

public class InvalidRoleNameException extends RoleException {

    public InvalidRoleNameException() {
        this("Nombre de Rol inválido.");
    }

    public InvalidRoleNameException(String message) {
        super(message);
    }

}