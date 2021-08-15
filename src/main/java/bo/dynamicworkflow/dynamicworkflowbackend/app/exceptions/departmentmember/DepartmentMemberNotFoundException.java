package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember;

public class DepartmentMemberNotFoundException extends DepartmentMemberException {

    public DepartmentMemberNotFoundException() {
        this("Miembro de Departamento no encontrado.");
    }

    public DepartmentMemberNotFoundException(String message) {
        super(message);
    }

    public DepartmentMemberNotFoundException(Integer departmentMemberId) {
        super(String.format("No se pudo encontrar el Miembro de Departamento con Id: %d.", departmentMemberId));
    }

}