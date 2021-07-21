package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Department;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DepartmentResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentMapper extends BaseMapper<DepartmentRequestDto, DepartmentResponseDto, Department> {

    @Override
    public Department toEntity(DepartmentRequestDto request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setContactEmail(request.getContactEmail());
        department.setContactPhone(request.getContactPhone());
        department.setLocation(request.getLocation());
        department.setParentDepartmentId(request.getParentDepartmentId());
        return department;
    }

    @Override
    public DepartmentResponseDto toDto(Department department) {
        if (department == null) return null;
        List<Department> subordinateDepartments = department.getSubordinateDepartments();
        return new DepartmentResponseDto(
                department.getId(),
                department.getName(),
                department.getContactEmail(),
                department.getContactPhone(),
                department.getLocation(),
                department.getCreationTimestamp(),
                department.getModificationTimestamp(),
                department.getStatus(),
                department.getParentDepartmentId(),
                subordinateDepartments != null && !subordinateDepartments.isEmpty() ?
                        subordinateDepartments.stream().map(this::toDto).collect(Collectors.toList()) : null
        );
    }

}