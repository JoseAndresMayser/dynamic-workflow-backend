package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.exceptions.GenerateTokenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.InvalidPasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.AccessRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RestorePasswordRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdatePasswordRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.AccessResponseDto;

public interface AccessService {

    AccessResponseDto logIn(AccessRequestDto request) throws UserException, InvalidPasswordException,
            GenerateTokenException;

    void restorePassword(RestorePasswordRequestDto request) throws UserException;

    void updatePassword(UpdatePasswordRequestDto request) throws UserException, InvalidPasswordException;

}