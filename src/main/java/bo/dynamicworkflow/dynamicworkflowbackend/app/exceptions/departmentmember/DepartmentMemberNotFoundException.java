package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember;

public class DepartmentMemberNotFoundException extends DepartmentMemberException {

    public DepartmentMemberNotFoundException() {
        this("Miembro del Departamento no encontrado.");
    }

    public DepartmentMemberNotFoundException(String message) {
        super(message);
    }

}