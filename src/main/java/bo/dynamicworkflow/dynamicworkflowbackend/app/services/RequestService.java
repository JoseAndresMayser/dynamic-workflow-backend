package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.NewRequestRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestResponseDto;

public interface RequestService {

    RequestResponseDto registerRequest(NewRequestRequestDto request) throws ProcessException, InputException,
            DirectoryCreationException, SaveFileException, RequestException, UserNotFoundException;

}