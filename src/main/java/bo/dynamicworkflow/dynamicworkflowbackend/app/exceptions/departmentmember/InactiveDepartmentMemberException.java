package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember;

public class InactiveDepartmentMemberException extends DepartmentMemberException {

    public InactiveDepartmentMemberException() {
        this("El miembro del departamento está inactivo.");
    }

    public InactiveDepartmentMemberException(String message) {
        super(message);
    }

    public InactiveDepartmentMemberException(Integer departmentMemberId) {
        super(String.format("El miembro de departamento con Id: %d está inactivo.", departmentMemberId));
    }

}