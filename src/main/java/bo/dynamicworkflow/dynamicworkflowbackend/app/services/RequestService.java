package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.DigitalCertificateNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.InvalidDigitalCertificatePasswordException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.InactiveProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.requeststage.RequestStageException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst.StageAnalystException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.NewRequestRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RequestActionRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestActionResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestResponseDto;

import java.util.List;

public interface RequestService {

    RequestResponseDto registerRequest(NewRequestRequestDto request) throws ProcessException, InputException,
            DirectoryCreationException, SaveFileException, RequestException, UserNotFoundException;

    RequestActionResponseDto executeActionOnRequest(RequestActionRequestDto request, Integer requestId)
            throws RequestException, InvalidDigitalCertificatePasswordException, StageAnalystException,
            InactiveProcessException, DepartmentMemberNotFoundException, RequestStageException,
            DigitalCertificateNotFoundException;

    List<RequestResponseDto> getAllRequestsForCurrentUser();

    List<RequestResponseDto> getPendingRequestsForCurrentAnalyst() throws DepartmentMemberNotFoundException;

    List<RequestResponseDto> getFinishedRequestsForCurrentAnalyst() throws DepartmentMemberNotFoundException;

}