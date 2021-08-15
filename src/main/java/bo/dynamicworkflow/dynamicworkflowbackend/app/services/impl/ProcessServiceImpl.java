package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.ForbiddenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.TimestampException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DisabledDepartmentException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.InactiveDepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputAlreadyExistsException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InvalidInputListException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InvalidInputNameException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.inputtype.InputTypeNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.process.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction.InvalidRestrictionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction.InvalidRestrictionValueException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction.RestrictionException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.restriction.RestrictionNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.selectioninputvalue.InvalidSelectionValueException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.selectioninputvalue.InvalidSelectionValueListException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.selectioninputvalue.SelectionInputValueException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stage.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst.InvalidStageAnalystListException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst.StageAnalystException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst.StageAnalystMandatoryApprovalException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Process;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.DepartmentStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.InputTypeName;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ProcessStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.ProcessService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.shared.DepartmentHandler;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessServiceImpl implements ProcessService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMemberRepository departmentMemberRepository;
    private final ProcessRepository processRepository;
    private final ProcessSchemaRepository processSchemaRepository;
    private final ProcessActivationRepository processActivationRepository;
    private final StageRepository stageRepository;
    private final StageAnalystRepository stageAnalystRepository;
    private final InputRepository inputRepository;
    private final InputTypeRepository inputTypeRepository;
    private final RestrictionRepository restrictionRepository;
    private final InputRestrictionRepository inputRestrictionRepository;
    private final SelectionInputValueRepository selectionInputValueRepository;
    private final TriggerSequenceRepository triggerSequenceRepository;

    private final ProcessMapper processMapper = new ProcessMapper();
    private final StageMapper stageMapper = new StageMapper();
    private final InputMapper inputMapper = new InputMapper();
    private final UserMapper userMapper = new UserMapper();
    private final DepartmentMapper departmentMapper = new DepartmentMapper();
    private final InputRestrictionMapper inputRestrictionMapper = new InputRestrictionMapper();
    private final SelectionInputValueMapper selectionInputValueMapper = new SelectionInputValueMapper();
    private final InputTypeMapper inputTypeMapper = new InputTypeMapper();

    @Autowired
    public ProcessServiceImpl(DepartmentRepository departmentRepository,
                              DepartmentMemberRepository departmentMemberRepository,
                              ProcessRepository processRepository, ProcessSchemaRepository processSchemaRepository,
                              ProcessActivationRepository processActivationRepository,
                              StageRepository stageRepository, StageAnalystRepository stageAnalystRepository,
                              InputRepository inputRepository, InputTypeRepository inputTypeRepository,
                              RestrictionRepository restrictionRepository,
                              InputRestrictionRepository inputRestrictionRepository,
                              SelectionInputValueRepository selectionInputValueRepository,
                              TriggerSequenceRepository triggerSequenceRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMemberRepository = departmentMemberRepository;
        this.processRepository = processRepository;
        this.processSchemaRepository = processSchemaRepository;
        this.processActivationRepository = processActivationRepository;
        this.stageRepository = stageRepository;
        this.stageAnalystRepository = stageAnalystRepository;
        this.inputRepository = inputRepository;
        this.inputTypeRepository = inputTypeRepository;
        this.restrictionRepository = restrictionRepository;
        this.inputRestrictionRepository = inputRestrictionRepository;
        this.selectionInputValueRepository = selectionInputValueRepository;
        this.triggerSequenceRepository = triggerSequenceRepository;
    }

    @Override
    @Transactional(rollbackOn = {TimestampException.class, StageException.class, InputException.class,
            InputTypeNotFoundException.class, RestrictionException.class, StageAnalystException.class,
            DepartmentMemberException.class, SelectionInputValueException.class})
    public CompleteProcessResponseDto createCompleteProcess(CompleteProcessRequestDto request)
            throws DepartmentException, DepartmentMemberException, ForbiddenException, ProcessException,
            TimestampException, StageException, StageAnalystException, InputException,
            InputTypeNotFoundException, RestrictionException, SelectionInputValueException {
        ProcessSchema processSchema = saveProcessAndGetSchema(request.getProcess());
        List<Stage> savedStages = saveStages(request.getStages(), processSchema.getId());
        saveInputs(request.getInputs(), processSchema, savedStages);
        return new CompleteProcessResponseDto("Finalizado.");
    }

    @Override
    public List<ProcessDetailedResponseDto> getAllDetailedProcesses() {
        List<Process> processes = processRepository.findAll();
        return processes.stream()
                .map(process -> new ProcessDetailedResponseDto(
                        processMapper.toDto(process),
                        userMapper.toDto(process.getUser()),
                        departmentMapper.toDto(process.getDepartment())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProcessResponseDto> getAllActiveProcessesByDepartmentId(Integer departmentId)
            throws DepartmentNotFoundException {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        List<Process> activeProcesses =
                processRepository.getAllByDepartmentIdAndStatus(department.getId(), ProcessStatus.ACTIVE);
        return processMapper.toDto(activeProcesses);
    }

    @Override
    public ProcessInputsResponseDto getProcessInputsByProcessId(Integer processId) throws ProcessException {
        Process process = processRepository.findById(processId)
                .orElseThrow(() -> new ProcessNotFoundException(processId));
        if (process.getStatus().equals(ProcessStatus.INACTIVE)) throw new InactiveProcessException();
        ProcessSchema processSchema = processSchemaRepository.findLastActiveByProcessId(process.getId())
                .orElseThrow(InactiveProcessException::new);
        List<Input> inputs = inputRepository.getAllByProcessSchemaId(processSchema.getId());
        List<InputDetailedResponseDto> detailedInputs = new ArrayList<>();
        inputs.forEach(input -> {
            InputType inputType = input.getInputType();
            InputDetailedResponseDto inputDetailed = new InputDetailedResponseDto();
            inputDetailed.setInput(inputMapper.toDto(input));
            inputDetailed.setInputType(inputTypeMapper.toDto(inputType));
            Integer inputId = input.getId();
            List<InputRestriction> inputRestrictions = inputRestrictionRepository.getAllByInputId(inputId);
            if (inputRestrictions != null && !inputRestrictions.isEmpty())
                inputDetailed.setRestrictions(inputRestrictionMapper.toDto(inputRestrictions));
            if (isInputTypeWithSelectionValues(inputType.getName())) {
                List<SelectionInputValue> selectionInputValues = selectionInputValueRepository.getAllByInputId(inputId);
                inputDetailed.setSelectionInputValues(selectionInputValueMapper.toDto(selectionInputValues));
            }
            detailedInputs.add(inputDetailed);
        });
        return new ProcessInputsResponseDto(processMapper.toDto(process), detailedInputs);
    }

    private ProcessSchema saveProcessAndGetSchema(ProcessRequestDto processRequest) throws DepartmentException,
            DepartmentMemberNotFoundException, ForbiddenException, ProcessException, TimestampException {
        Process savedProcess = saveProcessOnly(processRequest);
        ProcessSchema savedProcessSchema = saveProcessSchema(savedProcess.getId());
        saveProcessActivation(processRequest, savedProcessSchema.getId());
        return savedProcessSchema;
    }

    private Process saveProcessOnly(ProcessRequestDto processRequest) throws DepartmentException,
            DepartmentMemberNotFoundException, ForbiddenException, ProcessException {
        Department department = departmentRepository.findById(processRequest.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("No se pudo encontrar el departamento indicado."));
        if (department.getStatus().equals(DepartmentStatus.DISABLED))
            throw new DisabledDepartmentException("El departamento indicado se encuentra en estado DESHABILITADO.");
        DepartmentMember departmentMember = departmentMemberRepository
                .findLastActiveAssignmentByUserId(SessionHolder.getCurrentUserId())
                .orElseThrow(() -> new DepartmentMemberNotFoundException(
                        "El usuario actual no es miembro activo de ningún departamento."
                ));
        Department assignedDepartment = departmentMember.getDepartment();
        if (assignedDepartment == null)
            throw new DepartmentNotFoundException("No se pudo encontrar el departamento asignado al usuario actual.");
        if (!DepartmentHandler.hasPermitsInDepartment(assignedDepartment, department.getId()))
            throw new ForbiddenException(
                    "El usuario actual no tiene permisos para crear un proceso en el departamento indicado."
            );
        verifyIfProcessExists(processRequest.getName(), department.getId());

        Process process = processMapper.toEntity(processRequest);
        Timestamp currentTimestamp = TimeUtility.getCurrentTimestamp();
        process.setCreationTimestamp(currentTimestamp);
        process.setModificationTimestamp(currentTimestamp);
        process.setUserId(SessionHolder.getCurrentUserId());
        return processRepository.saveAndFlush(process);
    }

    private void verifyIfProcessExists(String processName, Integer departmentId) throws ProcessException {
        if (processName == null) throw new InvalidProcessNameException("El nombre del proceso no debe ser nulo.");
        List<Process> departmentProcesses = processRepository.getAllByDepartmentId(departmentId);
        for (Process departmentProcess : departmentProcesses)
            if (departmentProcess.getName().equalsIgnoreCase(processName))
                throw new ProcessAlreadyExistsException(String.format(
                        "Ya se encuentra registrado un proceso con el nombre: %s en el departamento indicado.",
                        processName
                ));
    }

    private ProcessSchema saveProcessSchema(Integer processId) {
        ProcessSchema processSchema = new ProcessSchema();
        processSchema.setIsActive(true);
        processSchema.setCreationTimestamp(TimeUtility.getCurrentTimestamp());
        processSchema.setHasTrigger(false);
        processSchema.setProcessId(processId);
        return processSchemaRepository.saveAndFlush(processSchema);
    }

    private void saveProcessActivation(ProcessRequestDto processRequest, Integer processSchemaId)
            throws TimestampException {
        ProcessActivation processActivation = new ProcessActivation();
        Timestamp startTimestamp = processRequest.getStartTimestamp();
        Timestamp finishTimestamp = processRequest.getFinishTimestamp();
        Boolean isIndeterminate = !hasStartAndFinishTimestamp(startTimestamp, finishTimestamp);
        processActivation.setIsIndeterminate(isIndeterminate);
        processActivation.setIsActive(true);
        if (!isIndeterminate) {
            processActivation.setStartTimestamp(startTimestamp);
            processActivation.setFinishTimestamp(finishTimestamp);
        }
        processActivation.setProcessSchemaId(processSchemaId);
        processActivationRepository.saveAndFlush(processActivation);
    }

    private Boolean hasStartAndFinishTimestamp(Timestamp startTimestamp, Timestamp finishTimestamp)
            throws TimestampException {
        if (startTimestamp == null || finishTimestamp == null) return false;
        if (!TimeUtility.isAfterAtCurrentTimestamp(startTimestamp))
            throw new TimestampException("La fecha de inicio debe ser superior a la fecha actual.");
        if (!TimeUtility.isAfterAt(finishTimestamp, startTimestamp))
            throw new TimestampException("La fecha de fin debe ser superior a la fecha de inicio.");
        return true;
    }

    private List<Stage> saveStages(List<StageRequestDto> stages, Integer processSchemaId) throws StageException,
            StageAnalystException, DepartmentMemberException {
        if (stages == null || stages.isEmpty())
            throw new InvalidStageListException("La lista de etapas no debe ser nula o estar vacía.");
        List<Stage> savedStages = new ArrayList<>();
        for (StageRequestDto stageRequest : stages) {
            Stage savedStage = saveStage(stageRequest, processSchemaId, stages.size());
            saveStageAnalysts(stageRequest.getAnalysts(), savedStage.getId());
            savedStages.add(savedStage);
        }
        List<Stage> updatedStages = new ArrayList<>();
        for (Stage savedStage : savedStages) {
            Stage updatedStage = updateStageWithIndexes(savedStage, savedStages);
            updatedStages.add(updatedStage);
        }
        return updatedStages;
    }

    private Stage saveStage(StageRequestDto stageRequest, Integer processSchemaId, Integer stagesAmount)
            throws StageException {
        verifyIfStageExists(stageRequest, processSchemaId);
        verifyApprovalsRequired(stageRequest);
        Integer stageIndex = stageRequest.getStageIndex();
        if (stageIndex < 1) throw new InvalidStageIndexException("El índice de la etapa no debe ser menor a 1.");
        if (stageIndex > stagesAmount)
            throw new InvalidStageIndexException(
                    "El índice de la etapa no debe ser mayor a la cantidad de etapas a registrar."
            );
        Integer previousStageIndex = stageRequest.getPreviousStageIndex();
        if (previousStageIndex != null && previousStageIndex >= stageIndex)
            throw new InvalidStageIndexException(
                    "El índice de la etapa anterior no debe ser mayor o igual al índice de la etapa actual."
            );
        Integer nextStageIndex = stageRequest.getNextStageIndex();
        if (nextStageIndex != null && nextStageIndex <= stageIndex)
            throw new InvalidStageIndexException(
                    "El índice de la etapa siguiente no debe ser menor o igual al índice de la etapa actual."
            );

        Stage stage = stageMapper.toEntity(stageRequest);
        stage.setHasConditional(false);
        stage.setProcessSchemaId(processSchemaId);
        return stageRepository.saveAndFlush(stage);
    }

    private void verifyIfStageExists(StageRequestDto stageRequest, Integer processSchemaId) throws StageException {
        String stageName = stageRequest.getName();
        if (stageName == null) throw new InvalidStageNameException("El nombre de la etapa no debe ser nulo.");
        Integer stageIndex = stageRequest.getStageIndex();
        if (stageIndex == null) throw new InvalidStageIndexException("El índice de la etapa no debe ser nulo.");
        List<Stage> stages = stageRepository.getAllByProcessSchemaId(processSchemaId);
        for (Stage stage : stages) {
            if (stage.getName().equalsIgnoreCase(stageName))
                throw new StageAlreadyExistsException(
                        String.format("Ya existe una etapa con el nombre: %s", stageName)
                );
            if (stage.getStageIndex().equals(stageIndex))
                throw new StageAlreadyExistsException(
                        String.format("Ya existe una etapa con el índice: %d", stageIndex)
                );
        }
    }

    private void verifyApprovalsRequired(StageRequestDto stageRequest) throws InvalidApprovalsRequiredException {
        Integer approvalsRequired = stageRequest.getApprovalsRequired();
        if (approvalsRequired < 1)
            throw new InvalidApprovalsRequiredException("La cantidad de aprobaciones requeridas debe ser al menos 1.");
        List<StageAnalystRequestDto> analysts = stageRequest.getAnalysts();
        if (approvalsRequired > analysts.size())
            throw new InvalidApprovalsRequiredException(
                    "La cantidad de aprobaciones requeridas no debe ser mayor a la cantidad de aprobadores."
            );
        long mandatoryApprovalsAmount = analysts.stream()
                .filter(stageAnalystRequest ->
                        stageAnalystRequest.getRequiresApproval() && stageAnalystRequest.getApprovalIsMandatory())
                .count();
        if (approvalsRequired < mandatoryApprovalsAmount)
            throw new InvalidApprovalsRequiredException(
                    "La cantidad de aprobaciones requeridas no debe ser menor a la cantidad de " +
                            "aprobaciones obligatorias."
            );
    }

    private void saveStageAnalysts(List<StageAnalystRequestDto> analysts, Integer stageId) throws StageAnalystException,
            DepartmentMemberException {
        if (analysts == null || analysts.isEmpty())
            throw new InvalidStageAnalystListException(
                    "La lista de analistas de la etapa no debe ser nula o estar vacía."
            );
        for (StageAnalystRequestDto stageAnalystRequest : analysts) {
            Boolean requiresApproval = stageAnalystRequest.getRequiresApproval();
            Boolean approvalIsMandatory = stageAnalystRequest.getApprovalIsMandatory();
            if (!requiresApproval && approvalIsMandatory)
                throw new StageAnalystMandatoryApprovalException(
                        "El analista no puede ser de aprobación obligatoria si no se requiere su aprobación."
                );
            Integer departmentMemberId = stageAnalystRequest.getDepartmentMemberId();
            DepartmentMember departmentMember = departmentMemberRepository.findById(departmentMemberId)
                    .orElseThrow(() -> new DepartmentMemberNotFoundException(departmentMemberId));
            if (!departmentMember.getIsActive()) throw new InactiveDepartmentMemberException(departmentMember.getId());

            StageAnalyst stageAnalyst = new StageAnalyst();
            stageAnalyst.setRequiresApproval(requiresApproval);
            stageAnalyst.setApprovalIsMandatory(approvalIsMandatory);
            stageAnalyst.setStageId(stageId);
            stageAnalyst.setDepartmentMemberId(departmentMember.getId());
            stageAnalystRepository.saveAndFlush(stageAnalyst);
        }
    }

    private Stage updateStageWithIndexes(Stage savedStage, List<Stage> savedStages) throws InvalidStageIndexException {
        Integer previousStageIndex = savedStage.getPreviousStageIndex();
        if (previousStageIndex != null) {
            Stage previousStage = findStageByIndex(savedStages, previousStageIndex);
            savedStage.setPreviousStageId(previousStage.getId());
        }
        Integer nextStageIndex = savedStage.getNextStageIndex();
        if (nextStageIndex != null) {
            Stage nextStage = findStageByIndex(savedStages, nextStageIndex);
            savedStage.setNextStageId(nextStage.getId());
        }
        return stageRepository.saveAndFlush(savedStage);
    }

    private Stage findStageByIndex(List<Stage> stages, Integer stageIndex) throws InvalidStageIndexException {
        return stages.stream()
                .filter(stage -> stage.getStageIndex().equals(stageIndex))
                .findFirst()
                .orElseThrow(() -> new InvalidStageIndexException(
                        String.format("No se pudo encontrar la etapa con índice: %d en la lista de etapas.", stageIndex)
                ));
    }

    private void saveInputs(List<InputRequestDto> inputs, ProcessSchema processSchema, List<Stage> savedStages)
            throws InputException, InputTypeNotFoundException, RestrictionException, SelectionInputValueException,
            InvalidStageIndexException, UseOfAllStagesException {
        if (inputs == null || inputs.isEmpty())
            throw new InvalidInputListException("La lista de campos del formulario no debe ser nula o estar vacía.");
        List<Integer> stageIndexesUsed = getStageIndexesUsed(savedStages);
        for (InputRequestDto inputRequest : inputs) {
            Integer inputTypeId = inputRequest.getInputTypeId();
            InputType inputType = inputTypeRepository.findById(inputTypeId)
                    .orElseThrow(() -> new InputTypeNotFoundException(inputTypeId));

            Input savedInput = saveInput(inputRequest, processSchema.getId(), inputType.getId());

            List<InputRestrictionRequestDto> inputRestrictions = inputRequest.getRestrictions();
            if (inputRestrictions != null && !inputRestrictions.isEmpty())
                saveInputRestrictions(inputType, inputRestrictions, savedInput.getId());

            if (isInputTypeWithSelectionValues(inputType.getName()))
                saveSelectionInputValues(
                        inputRequest.getSelectionValues(),
                        savedInput,
                        savedStages,
                        stageIndexesUsed,
                        processSchema
                );
        }
        if (savedStages.size() > 1) verifyIfAllStagesAreBeingUsed(stageIndexesUsed, savedStages.size());
    }

    private List<Integer> getStageIndexesUsed(List<Stage> stages) {
        List<Integer> stageIndexesUsed = new ArrayList<>();
        stages.forEach(stage -> {
            Integer previousStageIndex = stage.getPreviousStageIndex();
            if (!stageIndexesUsed.contains(previousStageIndex)) stageIndexesUsed.add(previousStageIndex);
            Integer nextStageIndex = stage.getNextStageIndex();
            if (!stageIndexesUsed.contains(nextStageIndex)) stageIndexesUsed.add(nextStageIndex);
        });
        return stageIndexesUsed;
    }

    private Input saveInput(InputRequestDto inputRequest, Integer processSchemaId, Integer inputTypeId)
            throws InputException {
        verifyIfInputExists(inputRequest.getName(), processSchemaId);
        Input input = inputMapper.toEntity(inputRequest);
        input.setIsTrigger(false);
        input.setProcessSchemaId(processSchemaId);
        input.setInputTypeId(inputTypeId);
        return inputRepository.saveAndFlush(input);
    }

    private void verifyIfInputExists(String inputName, Integer processSchemaId) throws InputException {
        if (inputName == null)
            throw new InvalidInputNameException("El nombre del campo del formulario no debe ser nulo.");
        List<Input> inputs = inputRepository.getAllByProcessSchemaId(processSchemaId);
        for (Input input : inputs)
            if (input.getName().equalsIgnoreCase(inputName))
                throw new InputAlreadyExistsException(
                        String.format("Ya existe un campo en el formulario con el nombre: %s", inputName)
                );
    }

    private void saveInputRestrictions(InputType inputType, List<InputRestrictionRequestDto> inputRestrictions,
                                       Integer inputId) throws RestrictionException {
        for (InputRestrictionRequestDto inputRestrictionRequest : inputRestrictions)
            saveInputRestriction(inputRestrictionRequest, inputType, inputId);
    }

    private void saveInputRestriction(InputRestrictionRequestDto inputRestrictionRequest, InputType inputType,
                                      Integer inputId) throws RestrictionException {
        String restrictionValue = inputRestrictionRequest.getValue();
        if (restrictionValue == null)
            throw new InvalidRestrictionValueException("El valor de la restricción no debe ser nulo.");
        Integer restrictionId = inputRestrictionRequest.getRestrictionId();
        Restriction restriction = restrictionRepository.findById(restrictionId)
                .orElseThrow(() -> new RestrictionNotFoundException(restrictionId));
        if (!isRestrictionOfInputType(inputType.getRestrictions(), restriction.getId()))
            throw new InvalidRestrictionException(String.format(
                    "La restricción con Id: %d no pertenece al grupo de restricciones del " +
                            "tipo de campo con Id: %d.",
                    restriction.getId(),
                    inputType.getId()
            ));
        if (restriction.getHasDefinedValues() &&
                !isValidValueOfDefinedRestrictions(restriction.getRestrictionDefinedValues(), restrictionValue))
            throw new InvalidRestrictionValueException(String.format(
                    "El valor de la restricción: %s no pertenece a los valores de restricción definidos.",
                    restrictionValue
            ));

        InputRestriction inputRestriction = new InputRestriction();
        inputRestriction.setValue(restrictionValue);
        inputRestriction.setInputId(inputId);
        inputRestriction.setRestrictionId(restriction.getId());
        inputRestrictionRepository.saveAndFlush(inputRestriction);
    }

    private Boolean isRestrictionOfInputType(List<Restriction> inputTypeRestrictions, Integer restrictionId) {
        if (inputTypeRestrictions == null) return false;
        return inputTypeRestrictions.stream()
                .anyMatch(inputRestriction -> inputRestriction.getId().equals(restrictionId));
    }

    private Boolean isValidValueOfDefinedRestrictions(List<RestrictionDefinedValue> restrictionDefinedValues,
                                                      String restrictionValue) {
        if (restrictionDefinedValues == null) return false;
        return restrictionDefinedValues.stream()
                .anyMatch(restrictionDefinedValue ->
                        restrictionDefinedValue.getValue().equalsIgnoreCase(restrictionValue));
    }

    private Boolean isInputTypeWithSelectionValues(InputTypeName inputTypeName) {
        return inputTypeName.equals(InputTypeName.MULTIPLE_CHOICE) ||
                inputTypeName.equals(InputTypeName.SELECTION_BOX) ||
                inputTypeName.equals(InputTypeName.DEPLOYABLE_LIST);
    }

    private void saveSelectionInputValues(List<SelectionInputValueRequestDto> selectionValues, Input savedInput,
                                          List<Stage> savedStages, List<Integer> stageIndexesUsed,
                                          ProcessSchema processSchema) throws SelectionInputValueException,
            InvalidStageIndexException {
        if (selectionValues == null || selectionValues.size() < 2)
            throw new InvalidSelectionValueListException(
                    "La lista de valores de selección del campo del formulario de tener al menos 2 elementos."
            );
        for (SelectionInputValueRequestDto selectionInputValueRequest : selectionValues)
            saveSelectionInputValue(
                    selectionInputValueRequest,
                    savedInput,
                    savedStages,
                    stageIndexesUsed,
                    processSchema
            );
    }

    private void saveSelectionInputValue(SelectionInputValueRequestDto selectionInputValueRequest, Input savedInput,
                                         List<Stage> savedStages, List<Integer> stageIndexesUsed,
                                         ProcessSchema processSchema) throws InvalidSelectionValueException,
            InvalidStageIndexException {
        String value = selectionInputValueRequest.getValue();
        if (value == null)
            throw new InvalidSelectionValueException(
                    "El valor de selección del campo de formulario no debe ser nulo."
            );
        SelectionInputValue selectionInputValue = new SelectionInputValue();
        selectionInputValue.setValue(value);
        selectionInputValue.setInputId(savedInput.getId());
        SelectionInputValue savedSelectionInputValue =
                selectionInputValueRepository.saveAndFlush(selectionInputValue);

        saveTriggerSequencesIfNecessary(
                selectionInputValueRequest.getTriggerSequences(),
                savedStages,
                stageIndexesUsed,
                savedSelectionInputValue.getId(),
                processSchema,
                savedInput
        );
    }

    private void saveTriggerSequencesIfNecessary(List<TriggerSequenceRequestDto> triggerSequences,
                                                 List<Stage> savedStages, List<Integer> stageIndexesUsed,
                                                 Integer selectionInputValueId, ProcessSchema processSchema,
                                                 Input savedInput) throws InvalidStageIndexException {
        if (triggerSequences == null || triggerSequences.isEmpty()) return;
        for (TriggerSequenceRequestDto triggerSequenceRequest : triggerSequences)
            saveTriggerSequence(triggerSequenceRequest, savedStages, stageIndexesUsed, selectionInputValueId);
        if (!processSchema.getHasTrigger()) {
            processSchema.setHasTrigger(true);
            processSchemaRepository.saveAndFlush(processSchema);
        }
        savedInput.setIsTrigger(true);
        inputRepository.saveAndFlush(savedInput);
    }

    private void saveTriggerSequence(TriggerSequenceRequestDto triggerSequenceRequest, List<Stage> savedStages,
                                     List<Integer> stageIndexesUsed, Integer selectionInputValueId)
            throws InvalidStageIndexException {
        TriggerSequence triggerSequence = new TriggerSequence();
        Integer nextStageIndex = triggerSequenceRequest.getNextStageIndex();
        Boolean hasNextStage = nextStageIndex != null;
        triggerSequence.setHasNextStage(hasNextStage);
        Integer currentStageIndex = triggerSequenceRequest.getCurrentStageIndex();
        if (currentStageIndex == null)
            throw new InvalidStageIndexException(
                    "El índice de la etapa actual no debe ser nulo para un desencadenador."
            );
        Stage currentStage = findStageByIndex(savedStages, currentStageIndex);
        if (!currentStage.getHasConditional()) {
            currentStage.setHasConditional(true);
            stageRepository.saveAndFlush(currentStage);
        }
        triggerSequence.setCurrentStageIndex(currentStageIndex);
        triggerSequence.setCurrentStageId(currentStage.getId());
        if (hasNextStage) {
            Stage nextStage = findStageByIndex(savedStages, nextStageIndex);
            triggerSequence.setNextStageIndex(nextStageIndex);
            triggerSequence.setNextStageId(nextStage.getId());
            if (!stageIndexesUsed.contains(nextStageIndex)) stageIndexesUsed.add(nextStageIndex);
        }
        triggerSequence.setSelectionInputValueId(selectionInputValueId);
        triggerSequenceRepository.saveAndFlush(triggerSequence);
    }

    private void verifyIfAllStagesAreBeingUsed(List<Integer> stageIndexesUsed, Integer stagesAmount)
            throws UseOfAllStagesException {
        for (Integer stageIndex = 1; stageIndex <= stagesAmount; stageIndex++)
            if (!stageIndexesUsed.contains(stageIndex)) throw new UseOfAllStagesException();
    }

}