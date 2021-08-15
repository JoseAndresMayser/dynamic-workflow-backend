package bo.dynamicworkflow.dynamicworkflowbackend.app.services.shared;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Department;

import java.util.List;

public class DepartmentHandler {

    public static Boolean hasPermitsInDepartment(Department department, Integer departmentIdToVerify) {
        if (department.getId().equals(departmentIdToVerify)) return true;
        List<Department> subordinateDepartments = department.getSubordinateDepartments();
        if (subordinateDepartments != null && !subordinateDepartments.isEmpty())
            for (Department subordinateDepartment : subordinateDepartments)
                if (hasPermitsInDepartment(subordinateDepartment, departmentIdToVerify)) return true;
        return false;
    }

}