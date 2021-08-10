package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DepartmentMember;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.DepartmentMemberDto;

public class DepartmentMemberMapper extends BaseMapper<DepartmentMemberDto, DepartmentMemberDto, DepartmentMember> {

    @Override
    public DepartmentMember toEntity(DepartmentMemberDto request) {
        throw new UnsupportedOperationException("Function not supported.");
    }

    @Override
    public DepartmentMemberDto toDto(DepartmentMember departmentMember) {
        DepartmentMemberDto departmentMemberDto = new DepartmentMemberDto();
        departmentMemberDto.setId(departmentMember.getId());
        departmentMemberDto.setIsDepartmentBoss(departmentMember.getIsDepartmentBoss());
        departmentMemberDto.setAssignmentTimestamp(departmentMember.getAssignmentTimestamp());
        departmentMemberDto.setIsActive(departmentMember.getIsActive());
        departmentMemberDto.setUserId(departmentMember.getUserId());
        departmentMemberDto.setDepartmentId(departmentMember.getDepartmentId());
        return departmentMemberDto;
    }

}