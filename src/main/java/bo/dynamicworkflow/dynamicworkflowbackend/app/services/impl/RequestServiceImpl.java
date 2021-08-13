package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InvalidInputValueException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.InactiveProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.ProcessNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.DuplicateRequestInputValueException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormGenerationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Process;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ProcessStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.RequestStageStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.RequestStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.NotificationService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RequestFormGeneratorService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RequestService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.FileInputValueRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.NewRequestRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.RequestInputValueRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.SelectionBoxInputValuesRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.RequestResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.RequestMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.Base64Utility;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.TimeUtility;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver.RequestFileManager;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver.SaveFileRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RequestServiceImpl implements RequestService {

    private final ProcessRepository processRepository;
    private final ProcessSchemaRepository processSchemaRepository;
    private final ProcessActivationRepository processActivationRepository;
    private final RequestRepository requestRepository;
    private final InputRepository inputRepository;
    private final SelectionInputValueRepository selectionInputValueRepository;
    private final RequestInputValueRepository requestInputValueRepository;
    private final StageRepository stageRepository;
    private final StageAnalystRepository stageAnalystRepository;
    private final RequestStageRepository requestStageRepository;
    private final UserRepository userRepository;
    private final DepartmentMemberRepository departmentMemberRepository;
    private final RequestActionRepository requestActionRepository;

    private final RequestFileManager requestFileManager;
    private final RequestFormGeneratorService requestFormGeneratorService;
    private final NotificationService notificationService;

    private final Gson gson = new Gson();
    private final RequestMapper requestMapper = new RequestMapper();

    @Autowired
    public RequestServiceImpl(ProcessRepository processRepository, ProcessSchemaRepository processSchemaRepository,
                              ProcessActivationRepository processActivationRepository,
                              RequestRepository requestRepository, InputRepository inputRepository,
                              SelectionInputValueRepository selectionInputValueRepository,
                              RequestInputValueRepository requestInputValueRepository,
                              StageRepository stageRepository, StageAnalystRepository stageAnalystRepository,
                              RequestStageRepository requestStageRepository, UserRepository userRepository,
                              DepartmentMemberRepository departmentMemberRepository,
                              RequestActionRepository requestActionRepository, RequestFileManager requestFileManager,
                              RequestFormGeneratorService requestFormGeneratorService,
                              NotificationService notificationService) {
        this.processRepository = processRepository;
        this.processSchemaRepository = processSchemaRepository;
        this.processActivationRepository = processActivationRepository;
        this.requestRepository = requestRepository;
        this.inputRepository = inputRepository;
        this.selectionInputValueRepository = selectionInputValueRepository;
        this.requestInputValueRepository = requestInputValueRepository;
        this.stageRepository = stageRepository;
        this.stageAnalystRepository = stageAnalystRepository;
        this.requestStageRepository = requestStageRepository;
        this.userRepository = userRepository;
        this.departmentMemberRepository = departmentMemberRepository;
        this.requestActionRepository = requestActionRepository;
        this.requestFileManager = requestFileManager;
        this.requestFormGeneratorService = requestFormGeneratorService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(rollbackOn = {InputException.class, DirectoryCreationException.class, SaveFileException.class,
            RequestException.class, UserNotFoundException.class})
    public RequestResponseDto registerRequest(NewRequestRequestDto request) throws ProcessException, InputException,
            DirectoryCreationException, SaveFileException, RequestException, UserNotFoundException {
        Integer processId = request.getProcessId();
        ProcessSchema processSchema = getProcessSchemaOrFail(processId);
        Request savedRequest = saveRequest(processId);
        saveRequestInputValues(request.getInputValues(), savedRequest);

        User currentUser = userRepository.findById(SessionHolder.getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException("No se pudo encontrar el Usuario actual."));
        String processName = processSchema.getProcess().getName();
        Request updatedRequest = generateAndSaveRequestForm(processName, savedRequest, currentUser);

        goToFirstStageAndNotifyAnalysts(processSchema.getId(), updatedRequest, currentUser.fullName(), processName);

        notificationService.sendRegisteredNewRequestNotification(
                currentUser.getEmail(),
                processName,
                updatedRequest.getCode(),
                requestFileManager.getAbsolutePath(updatedRequest.getFormPath())
        );

        return requestMapper.toDto(updatedRequest);
    }

    @Override
    public List<RequestResponseDto> getAllRequestsFromCurrentUser() {
        List<Request> requests = requestRepository.getAllByUserId(SessionHolder.getCurrentUserId());
        return requestMapper.toDto(requests);
    }

    @Override
    public List<RequestResponseDto> getPendingRequestsForCurrentAnalyst() throws DepartmentMemberNotFoundException {
        DepartmentMember currentDepartmentMember =
                departmentMemberRepository.findLastActiveAssignmentByUserId(SessionHolder.getCurrentUserId())
                        .orElseThrow(() -> new DepartmentMemberNotFoundException(
                                "El usuario actual no es Miembro activo de ningún Departamento."
                        ));
        List<StageAnalyst> stageAnalysts =
                stageAnalystRepository.getAllByDepartmentMemberId(currentDepartmentMember.getId());
        List<Request> requests = new ArrayList<>();
        stageAnalysts.forEach(stageAnalyst -> {
            Stage stage = stageAnalyst.getStage();
            List<RequestStage> requestStages = requestStageRepository.getAllByStageId(stage.getId());
            requestStages.stream()
                    .filter(requestStage -> !requestStage.getStatus().equals(RequestStageStatus.FINISHED))
                    .map(RequestStage::getRequest)
                    .forEach(request -> {
                        Optional<RequestAction> optionalRequestAction = requestActionRepository
                                .findByRequestIdAndStageAnalystId(request.getId(), stageAnalyst.getId());
                        if (!optionalRequestAction.isPresent()) requests.add(request);
                    });
        });
        return requestMapper.toDto(requests);
    }

    private ProcessSchema getProcessSchemaOrFail(Integer processId) throws ProcessException {
        Process process = processRepository.findById(processId)
                .orElseThrow(() -> new ProcessNotFoundException(processId));
        if (process.getStatus().equals(ProcessStatus.INACTIVE)) throw new InactiveProcessException();
        Optional<ProcessSchema> optionalProcessSchema =
                processSchemaRepository.findLastActiveByProcessId(process.getId());
        if (!optionalProcessSchema.isPresent()) {
            deactivateProcess(process);
            throw new InactiveProcessException();
        }
        Optional<ProcessActivation> optionalProcessActivation =
                processActivationRepository.findLastActiveByProcessSchemaId(optionalProcessSchema.get().getId());
        if (!optionalProcessActivation.isPresent()) {
            deactivateProcess(process);
            throw new InactiveProcessException();
        }
        ProcessActivation processActivation = optionalProcessActivation.get();
        if (!processActivation.getIsIndeterminate() &&
                !TimeUtility.isAfterAtCurrentTimestamp(processActivation.getFinishTimestamp())) {
            processActivation.setIsActive(false);
            processActivationRepository.saveAndFlush(processActivation);
            deactivateProcess(process);
            throw new InactiveProcessException();
        }
        return optionalProcessSchema.get();
    }

    private void deactivateProcess(Process process) {
        process.setStatus(ProcessStatus.INACTIVE);
        processRepository.saveAndFlush(process);
    }

    private Request saveRequest(Integer processId) {
        Request newRequest = new Request();
        newRequest.setShippingTimestamp(TimeUtility.getCurrentTimestamp());
        newRequest.setStatus(RequestStatus.IN_PROCESS);
        newRequest.setProcessId(processId);
        newRequest.setUserId(SessionHolder.getCurrentUserId());
        Request savedRequest = requestRepository.saveAndFlush(newRequest);
        savedRequest.setCode(generateRequestCode(savedRequest.getId()));
        return requestRepository.saveAndFlush(savedRequest);
    }

    private String generateRequestCode(Integer requestId) {
        int length = String.valueOf(requestId).length();
        String zeros = IntStream.range(0, (8 - length)).mapToObj(i -> "0").collect(Collectors.joining());
        return "DW" + zeros + requestId;
    }

    private void saveRequestInputValues(List<RequestInputValueRequestDto> inputValues, Request savedRequest)
            throws InputException, DuplicateRequestInputValueException, SaveFileException, DirectoryCreationException {
        List<RequestInputValue> requestInputValuesToSave = new ArrayList<>();
        for (RequestInputValueRequestDto requestInputValueRequest : inputValues) {
            Integer inputId = requestInputValueRequest.getInputId();
            Input input = inputRepository.findById(inputId).orElseThrow(() -> new InputNotFoundException(inputId));
            for (RequestInputValue requestInputValueToSave : requestInputValuesToSave)
                if (requestInputValueToSave.getInputId().equals(input.getId()))
                    throw new DuplicateRequestInputValueException();
            String value = processInputValue(requestInputValueRequest.getValue(), input, savedRequest);
            RequestInputValue requestInputValue = new RequestInputValue();
            requestInputValue.setValue(value);
            requestInputValue.setInputId(input.getId());
            requestInputValue.setRequestId(savedRequest.getId());
            requestInputValuesToSave.add(requestInputValue);
        }
        requestInputValueRepository.saveAll(requestInputValuesToSave);
    }

    private String processInputValue(String value, Input input, Request request) throws InvalidInputValueException,
            DirectoryCreationException, SaveFileException {
        Integer inputId = input.getId();
        switch (input.getInputType().getName()) {
            case TEXT:
                return getTextValue(value);
            case MULTIPLE_CHOICE:
            case DEPLOYABLE_LIST:
                return getSelectionValue(value, inputId);
            case SELECTION_BOX:
                return getSelectionBoxValue(value, inputId);
            case UPLOAD_FILE:
                return getFilePathValue(value, input.getName(), request);
            case DATE:
                return getDateValue(value);
        }
        throw new InvalidInputValueException("La entrada del formulario no cuenta con un tipo de entrada válida.");
    }

    private String getTextValue(String value) throws InvalidInputValueException {
        if (value == null || value.isEmpty())
            throw new InvalidInputValueException("El valor de la entrada no debe ser nulo o vacío.");
        return value;
    }

    private String getSelectionValue(String value, Integer inputId) throws InvalidInputValueException {
        String inputValue = getTextValue(value);
        List<SelectionInputValue> selectionInputValues = selectionInputValueRepository.getAllByInputId(inputId);
        boolean valueExists = selectionInputValues.stream()
                .anyMatch(selectionInputValue -> selectionInputValue.getValue().trim().equals(inputValue.trim()));
        if (!valueExists)
            throw new InvalidInputValueException(
                    "El valor de la entrada no coincide con ninguno de los valores de selección definidos."
            );
        return inputValue;
    }

    private String getSelectionBoxValue(String value, Integer inputId) throws InvalidInputValueException {
        SelectionBoxInputValuesRequestDto selectionBoxInputValuesRequest;
        try {
            selectionBoxInputValuesRequest = gson.fromJson(value, SelectionBoxInputValuesRequestDto.class);
        } catch (JsonSyntaxException exception) {
            throw new InvalidInputValueException(
                    "El valor de la entrada es incorrecto, se esperaba un objeto con una lista de valores de " +
                            "tipo TEXTO."
            );
        }
        List<String> inputValues = selectionBoxInputValuesRequest.getInputValues();
        if (inputValues == null || inputValues.isEmpty())
            throw new InvalidInputValueException(
                    "La lista de valores seleccionados no debe ser nula o estar vacía."
            );
        List<SelectionInputValue> selectionInputValues = selectionInputValueRepository.getAllByInputId(inputId);
        boolean allValuesExist = true;
        for (String selectionBoxValue : inputValues) {
            boolean selectionBoxValueExists = selectionInputValues.stream()
                    .anyMatch(selectionInputValue ->
                            selectionInputValue.getValue().trim().equals(selectionBoxValue.trim()));
            if (!selectionBoxValueExists) {
                allValuesExist = false;
                break;
            }
        }
        if (!allValuesExist)
            throw new InvalidInputValueException(
                    "Algunos valores de selección enviados no coinciden con los valores de selección definidos."
            );
        StringBuilder result = new StringBuilder();
        for (int index = 0, inputValuesSize = inputValues.size(); index < inputValuesSize; index++) {
            String selectionBoxValue = inputValues.get(index);
            result.append(selectionBoxValue);
            if (index < inputValuesSize - 1) result.append(", ");
        }
        return result.toString();
    }

    private String getFilePathValue(String value, String inputName, Request request) throws InvalidInputValueException,
            DirectoryCreationException, SaveFileException {
        FileInputValueRequestDto fileInputValueRequest;
        try {
            fileInputValueRequest = gson.fromJson(value, FileInputValueRequestDto.class);
        } catch (JsonSyntaxException exception) {
            throw new InvalidInputValueException(
                    "El valor de la entrada es incorrecto, se esperaba un formato de entrada de tipo ARCHIVO."
            );
        }
        if (fileInputValueRequest == null)
            throw new InvalidInputValueException("El valor de la entrada de tipo ARCHIVO no debe ser nulo.");
        SaveFileRequest saveFileRequest = new SaveFileRequest(
                inputName,
                fileInputValueRequest.getFileContent(),
                fileInputValueRequest.getExtension()
        );
        return requestFileManager.saveRequestFile(request.getShippingTimestamp(), request.getId(), saveFileRequest);
    }

    private String getDateValue(String value) throws InvalidInputValueException {
        if (value == null)
            throw new InvalidInputValueException("El valor de la entrada de tipo FECHA no debe ser nulo.");
        return value;
    }

    private Request generateAndSaveRequestForm(String processName, Request request, User user)
            throws RequestFormGenerationException, SaveFileException, DirectoryCreationException {
        byte[] requestFormContent = requestFormGeneratorService.generateRequestForm(processName, request, user);
        SaveFileRequest requestFormFile = new SaveFileRequest(
                "Formulario de solicitud-" + request.getCode() + "-(sin firmar)",
                Base64Utility.encodeAsString(requestFormContent),
                "pdf"
        );
        String requestFormPath = requestFileManager.saveRequestFile(
                request.getShippingTimestamp(),
                request.getId(),
                requestFormFile
        );
        request.setFormPath(requestFormPath);
        return requestRepository.saveAndFlush(request);
    }

    private void goToFirstStageAndNotifyAnalysts(Integer processSchemaId, Request request, String userFullName,
                                                 String processName) {
        List<Stage> stages = stageRepository.getAllByProcessSchemaId(processSchemaId);
        Stage fistStage = getFirstStage(stages);
        RequestStage requestStage = new RequestStage();
        requestStage.setEntryTimestamp(TimeUtility.getCurrentTimestamp());
        requestStage.setStatus(RequestStageStatus.PENDING);
        requestStage.setRequestId(request.getId());
        requestStage.setStageId(fistStage.getId());
        requestStageRepository.saveAndFlush(requestStage);
        List<StageAnalyst> stageAnalysts = stageAnalystRepository.getAllByStageId(fistStage.getId());
        List<String> analystEmails = stageAnalysts.stream()
                .map(stageAnalyst -> stageAnalyst.getDepartmentMember().getUser())
                .map(User::getEmail)
                .collect(Collectors.toList());
        notificationService.sendNotificationToStageAnalysts(
                analystEmails,
                userFullName,
                processName,
                request.getCode(),
                requestFileManager.getAbsolutePath(request.getFormPath())
        );
    }

    private Stage getFirstStage(List<Stage> stages) {
        Stage fistStage = stages.get(0);
        for (int index = 1; index < stages.size(); index++) {
            Stage stage = stages.get(index);
            if (stage.getStageIndex() < fistStage.getStageIndex()) fistStage = stage;
        }
        return fistStage;
    }

}