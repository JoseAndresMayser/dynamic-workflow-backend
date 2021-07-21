package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember;

public class UserAlreadyDepartmentMemberException extends DepartmentMemberException {

    public UserAlreadyDepartmentMemberException() {
        this("El usuario ya es Miembro de un Departamento.");
    }

    public UserAlreadyDepartmentMemberException(String message) {
        super(message);
    }

    public UserAlreadyDepartmentMemberException(Integer userId) {
        this(String.format("El Usuario con Id: %d ya es Miembro de un Departamento.", userId));
    }

}