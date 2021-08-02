package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidEmailException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidPasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.role.RoleException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserResponseDto registerRequestingUser(UserRequestDto request) throws UserException, InvalidEmailException,
        InvalidPasswordException, RoleException, ActionException;

    UserActionResponseDto registerUser(CompleteUserRequestDto request) throws UserException, InvalidEmailException,
        InvalidPasswordException, ActionException;

    UserActionResponseDto updateUser(CompleteUserRequestDto request, Integer userId) throws UserException,
        InvalidEmailException, InvalidPasswordException, ActionException;

    UserResponseDto updateCurrentUser(UserRequestDto request) throws UserException, InvalidEmailException,
        InvalidPasswordException;

    UserResponseDto getByUserId(Integer userId) throws UserException;

    List<UserResponseDto> getAllUsers();

    UserActionResponseDto getUserActionsByUserId(Integer userId) throws UserNotFoundException;

    List<UserResponseDto> getNonDepartmentBosses();

    List<UserResponseDto> getNonDepartmentMembers();

}
