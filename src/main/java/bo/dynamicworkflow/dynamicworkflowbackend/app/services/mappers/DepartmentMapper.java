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
        DepartmentResponseDto departmentResponseDto = internalToDto(department);
        departmentResponseDto.setParentDepartment(internalToDto(department.getParentDepartment()));
        List<Department> subordinateDepartments = department.getSubordinateDepartments();
        if (subordinateDepartments != null && !subordinateDepartments.isEmpty())
            departmentResponseDto.setSubordinateDepartments(
                    subordinateDepartments.stream().map(this::toDto).collect(Collectors.toList())
            );
        return departmentResponseDto;
    }

    private DepartmentResponseDto internalToDto(Department department) {
        if (department == null) return null;
        DepartmentResponseDto departmentResponseDto = new DepartmentResponseDto();
        departmentResponseDto.setId(department.getId());
        departmentResponseDto.setName(department.getName());
        departmentResponseDto.setContactEmail(department.getContactEmail());
        departmentResponseDto.setContactPhone(department.getContactPhone());
        departmentResponseDto.setLocation(department.getLocation());
        departmentResponseDto.setCreationTimestamp(department.getCreationTimestamp());
        departmentResponseDto.setModificationTimestamp(department.getModificationTimestamp());
        departmentResponseDto.setStatus(department.getStatus());
        departmentResponseDto.setParentDepartmentId(department.getParentDepartmentId());
        return departmentResponseDto;
    }

}