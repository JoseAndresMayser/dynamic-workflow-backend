package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UserRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserResponseDto;

public class UserMapper extends BaseMapper<UserRequestDto, UserResponseDto, User> {

    @Override
    public User toEntity(UserRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setNames(request.getNames());
        user.setFirstSurname(request.getFirstSurname());
        user.setSecondSurname(request.getSecondSurname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setIdentificationNumber(request.getIdentificationNumber());
        user.setCode(request.getCode());
        return user;
    }

    @Override
    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getStatus(),
                user.getCreationTimestamp(),
                user.getModificationTimestamp(),
                user.getNames(),
                user.getFirstSurname(),
                user.getSecondSurname(),
                user.getEmail(),
                user.getPhone(),
                user.getIdentificationNumber(),
                user.getCode()
        );
    }

}