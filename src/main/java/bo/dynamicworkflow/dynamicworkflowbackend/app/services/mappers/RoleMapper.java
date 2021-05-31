package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Role;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RoleRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RoleResponseDto;

public class RoleMapper extends BaseMapper<RoleRequestDto, RoleResponseDto, Role> {

    @Override
    public Role toEntity(RoleRequestDto request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        return role;
    }

    @Override
    public RoleResponseDto toDto(Role role) {
        return new RoleResponseDto(role.getId(), role.getName(), role.getDescription());
    }

}