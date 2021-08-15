package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.UserAlreadyDepartmentMemberException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Department;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DepartmentMember;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.DepartmentStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.DepartmentMemberRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.DepartmentRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.UserRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.DepartmentService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.DepartmentMemberDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteDepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.UpdateDepartmentMembersRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.CompleteDepartmentResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DepartmentResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.DepartmentMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.DepartmentMemberMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.UserMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final DepartmentMemberRepository departmentMemberRepository;

    private final DepartmentMapper departmentMapper = new DepartmentMapper();
    private final UserMapper userMapper = new UserMapper();
    private final DepartmentMemberMapper departmentMemberMapper = new DepartmentMemberMapper();

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, UserRepository userRepository,
                                 DepartmentMemberRepository departmentMemberRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.departmentMemberRepository = departmentMemberRepository;
    }

    @Override
    @Transactional(rollbackOn = {DepartmentException.class, UserException.class})
    public CompleteDepartmentResponseDto registerCompleteDepartment(CompleteDepartmentRequestDto request)
            throws DepartmentException, UserAlreadyDepartmentMemberException, UserException {
        DepartmentRequestDto departmentRequest = request.getDepartment();
        verifyDepartmentNameForParent(
                departmentRequest.getName(),
                departmentRequest.getParentDepartmentId(),
                null
        );
        DepartmentStatus status = getDepartmentStatus(departmentRequest.getStatus());
        User departmentBoss = getDepartmentBossOrFail(request.getDepartmentBossId());
        Integer departmentBossId = departmentBoss.getId();
        Department department = departmentMapper.toEntity(departmentRequest);
        Timestamp currentTimestamp = TimeUtility.getCurrentTimestamp();
        department.setCreationTimestamp(currentTimestamp);
        department.setModificationTimestamp(currentTimestamp);
        department.setStatus(status);
        Department registeredDepartment = departmentRepository.saveAndFlush(department);
        Integer departmentId = registeredDepartment.getId();
        DepartmentMember departmentBossMember = registerNewDepartmentBoss(departmentBossId, departmentId);
        List<Integer> analystMembersId = request.getAnalystMembersId();
        verifyAnalystMembersId(analystMembersId, departmentBossId);
        List<DepartmentMember> analystMembers = registerAnalystMembers(analystMembersId, departmentId);
        return new CompleteDepartmentResponseDto(
                departmentMapper.toDto(registeredDepartment),
                departmentMemberMapper.toDto(departmentBossMember),
                departmentMemberMapper.toDto(analystMembers)
        );
    }

    @Override
    public DepartmentResponseDto updateDepartment(DepartmentRequestDto request, Integer departmentId)
            throws DepartmentException {
        Department departmentToUpdate = getOptionalDepartmentById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        String newDepartmentName = request.getName();
        verifyDepartmentNameForParent(
                newDepartmentName,
                departmentToUpdate.getParentDepartmentId(),
                departmentToUpdate.getName()
        );
        DepartmentStatus newStatus = getDepartmentStatus(request.getStatus());
        departmentToUpdate.setName(newDepartmentName);
        departmentToUpdate.setContactEmail(request.getContactEmail());
        departmentToUpdate.setContactPhone(request.getContactPhone());
        departmentToUpdate.setLocation(request.getLocation());
        departmentToUpdate.setModificationTimestamp(TimeUtility.getCurrentTimestamp());
        departmentToUpdate.setStatus(newStatus);
        departmentToUpdate.setParentDepartmentId(request.getParentDepartmentId());
        Department updatedDepartment = departmentRepository.saveAndFlush(departmentToUpdate);
        return departmentMapper.toDto(updatedDepartment);
    }

    @Override
    @Transactional(rollbackOn = {DepartmentException.class, UserNotFoundException.class})
    public CompleteDepartmentResponseDto updateDepartmentMembers(UpdateDepartmentMembersRequestDto request,
                                                                 Integer departmentId) throws DepartmentException,
            UserNotFoundException, DepartmentMemberException {
        Department department = getOptionalDepartmentById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        User newDepartmentBoss = getNewDepartmentBossOrFail(request.getDepartmentBossId(), departmentId);
        User oldDepartmentBoss = getOldDepartmentBossByDepartmentId(departmentId);
        Integer newDepartmentBossId = newDepartmentBoss.getId();
        DepartmentMember departmentBossMember =
                updateDepartmentBossIfNecessary(newDepartmentBossId, oldDepartmentBoss.getId(), departmentId);
        List<Integer> newAnalystMembersId = request.getAnalystMembersId();
        verifyAnalystMembersId(newAnalystMembersId, newDepartmentBossId);
        List<DepartmentMember> currentAnalystMembers =
                departmentMemberRepository.getAllActiveAnalystMembersByDepartmentId(departmentId);
        List<DepartmentMember> analystMembersToDeactivate = currentAnalystMembers
                .stream()
                .filter(currentAnalystMember -> !newAnalystMembersId.remove(currentAnalystMember.getUserId()))
                .collect(Collectors.toList());
        deactivateAnalystMembers(analystMembersToDeactivate);
        List<DepartmentMember> newAnalystMembers = registerAnalystMembers(newAnalystMembersId, departmentId);
        department.setModificationTimestamp(TimeUtility.getCurrentTimestamp());
        Department updatedDepartment = departmentRepository.saveAndFlush(department);
        return new CompleteDepartmentResponseDto(
                departmentMapper.toDto(updatedDepartment),
                departmentMemberMapper.toDto(departmentBossMember),
                departmentMemberMapper.toDto(newAnalystMembers)
        );
    }

    @Override
    public CompleteDepartmentResponseDto getCompleteDepartmentById(Integer departmentId)
            throws DepartmentNotFoundException, DepartmentMemberNotFoundException {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        DepartmentMember departmentBossMember =
                departmentMemberRepository.findDepartmentBossMemberByDepartmentId(departmentId)
                        .orElseThrow(() -> new DepartmentMemberNotFoundException(
                                "Miembro Jefe del Departamento no encontrado."
                        ));
        DepartmentMemberDto departmentBossMemberDto = departmentMemberMapper.toDto(departmentBossMember);
        departmentBossMemberDto.setUser(userMapper.toDto(departmentBossMember.getUser()));
        departmentBossMemberDto.setDepartment(departmentMapper.toDto(departmentBossMember.getDepartment()));
        List<DepartmentMember> analystMembers =
                departmentMemberRepository.getAllActiveAnalystMembersByDepartmentId(departmentId);
        List<DepartmentMemberDto> analystMembersDto = new ArrayList<>();
        analystMembers.forEach(analystMember -> {
            DepartmentMemberDto analystMemberDto = departmentMemberMapper.toDto(analystMember);
            analystMemberDto.setUser(userMapper.toDto(analystMember.getUser()));
            analystMemberDto.setDepartment(departmentMapper.toDto(analystMember.getDepartment()));
            analystMembersDto.add(analystMemberDto);
        });
        return new CompleteDepartmentResponseDto(
                departmentMapper.toDto(department),
                departmentBossMemberDto,
                analystMembersDto
        );
    }

    @Override
    public List<DepartmentResponseDto> getAllDepartmentsForCurrentUser() throws DepartmentMemberNotFoundException,
            DepartmentNotFoundException {
        DepartmentMember departmentMember =
                getLastActiveOptionalDepartmentMemberByUserId(SessionHolder.getCurrentUserId())
                        .orElseThrow(() -> new DepartmentMemberNotFoundException(
                                "El Usuario actual no es Miembro activo de ningún Departamento."
                        ));
        Department parentDepartment = departmentMember.getDepartment();
        if (parentDepartment == null)
            throw new DepartmentNotFoundException("No se logró encontrar el Departamento asignado al Usuario actual.");
        List<Department> departments = new ArrayList<>();
        fillListWithDepartments(departments, parentDepartment);
        return departmentMapper.toDto(departments);
    }

    @Override
    public List<DepartmentResponseDto> getDepartmentWithDescendantsById(Integer departmentId)
            throws DepartmentNotFoundException {
        Department department = getOptionalDepartmentById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        List<Department> departments = new ArrayList<>();
        fillListWithDepartments(departments, department);
        return departmentMapper.toDto(departments);
    }

    @Override
    public DepartmentResponseDto getRootDepartment() throws DepartmentNotFoundException {
        Department rootDepartment = departmentRepository.findRootDepartment()
                .orElseThrow(() -> new DepartmentNotFoundException("No se pudo encontrar el departamento raíz."));
        return departmentMapper.toDto(rootDepartment);
    }

    private void verifyDepartmentNameForParent(String departmentName, Integer parentDepartmentId,
                                               String oldDepartmentName) throws DepartmentException {
        verifyIfDepartmentNameIsNull(departmentName);
        Department parentDepartment = getParentDepartmentOrFail(parentDepartmentId);
        if (parentDepartment == null) return;
        if (departmentName.equalsIgnoreCase(parentDepartment.getName()))
            throw new DepartmentAlreadyExistsException(
                    "El Departamento Subordinado no puede tener el mismo nombre que su Departamento Padre."
            );
        List<Department> subordinateDepartments = parentDepartment.getSubordinateDepartments();
        if (subordinateDepartments == null || subordinateDepartments.isEmpty()) return;
        for (Department subordinateDepartment : subordinateDepartments)
            if (!subordinateDepartment.getName().equals(oldDepartmentName) &&
                    subordinateDepartment.getName().equalsIgnoreCase(departmentName))
                throw new DepartmentAlreadyExistsException(
                        String.format("Ya se encuentra registrado un Departamento con el nombre: %s.", departmentName)
                );
    }

    private void verifyIfDepartmentNameIsNull(String departmentName) throws InvalidDepartmentNameException {
        if (departmentName == null)
            throw new InvalidDepartmentNameException("El Nombre del Departamento no debe ser nulo.");
    }

    private Department getParentDepartmentOrFail(Integer parentDepartmentId) throws DepartmentException {
        if (parentDepartmentId == null) {
            if (departmentRepository.findRootDepartment().isPresent()) throw new RootDepartmentAlreadyExistsException();
            return null;
        }
        return getOptionalDepartmentById(parentDepartmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        String.format("No se pudo encontrar el Departamento Padre con Id: %d.", parentDepartmentId)
                ));
    }

    private Optional<Department> getOptionalDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId);
    }

    private DepartmentStatus getDepartmentStatus(String departmentStatus) throws DepartmentStatusException {
        try {
            if (departmentStatus != null) return DepartmentStatus.valueOf(departmentStatus);
            throw new DepartmentStatusException("El Estado del Departamento no debe ser nulo.");
        } catch (IllegalArgumentException exception) {
            throw new DepartmentStatusException();
        }
    }

    private User getDepartmentBossOrFail(Integer departmentBossId) throws InvalidDepartmentBossException,
            UserNotFoundException {
        verifyIfDepartmentBossIdIsNull(departmentBossId);
        Optional<DepartmentMember> optionalDepartmentMember =
                getLastActiveOptionalDepartmentMemberByUserId(departmentBossId);
        if (optionalDepartmentMember.isPresent() && optionalDepartmentMember.get().getIsDepartmentBoss())
            throw new InvalidDepartmentBossException(
                    String.format("El Usuario con Id: %d ya es Jefe de un Departamento.", departmentBossId)
            );
        return getDepartmentBossByDepartmentBossId(departmentBossId);
    }

    private void verifyIfDepartmentBossIdIsNull(Integer departmentBossId) throws InvalidDepartmentBossException {
        if (departmentBossId == null)
            throw new InvalidDepartmentBossException("El Id del Jefe de Departamento no debe ser nulo.");
    }

    private Optional<DepartmentMember> getLastActiveOptionalDepartmentMemberByUserId(Integer userId) {
        return departmentMemberRepository.findLastActiveAssignmentByUserId(userId);
    }

    private User getDepartmentBossByDepartmentBossId(Integer departmentBossId) throws UserNotFoundException {
        return getOptionalUserById(departmentBossId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("No se pudo encontrar un Jefe de Departamento con Id: %d.", departmentBossId)
                ));
    }

    private Optional<User> getOptionalUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    private DepartmentMember registerNewDepartmentBoss(Integer newDepartmentBossId, Integer departmentId) {
        Optional<DepartmentMember> optionalDepartmentMember =
                getLastActiveOptionalDepartmentMemberByUserId(newDepartmentBossId);
        if (optionalDepartmentMember.isPresent()) {
            DepartmentMember oldDepartmentMember = optionalDepartmentMember.get();
            oldDepartmentMember.setIsActive(false);
            departmentMemberRepository.saveAndFlush(oldDepartmentMember);
        }
        DepartmentMember newDepartmentMember = new DepartmentMember();
        newDepartmentMember.setIsDepartmentBoss(true);
        newDepartmentMember.setAssignmentTimestamp(TimeUtility.getCurrentTimestamp());
        newDepartmentMember.setIsActive(true);
        newDepartmentMember.setUserId(newDepartmentBossId);
        newDepartmentMember.setDepartmentId(departmentId);
        return departmentMemberRepository.saveAndFlush(newDepartmentMember);
    }

    private void verifyAnalystMembersId(List<Integer> analystMembersId, Integer departmentBossId)
            throws InvalidAnalystMembersIdListException {
        if (analystMembersId == null)
            throw new InvalidAnalystMembersIdListException("La lista de Id de analistas no debe ser nula.");
        if (analystMembersId.isEmpty())
            throw new InvalidAnalystMembersIdListException(
                    "La lista de Id de analistas debe contener al menos un elemento."
            );
        if (analystMembersId.contains(departmentBossId))
            throw new InvalidAnalystMembersIdListException(
                    "La lista de Analistas no debe contener al Jefe de Departamento a designar."
            );
    }

    private List<DepartmentMember> registerAnalystMembers(List<Integer> analystMembersId, Integer departmentId)
            throws UserNotFoundException, UserAlreadyDepartmentMemberException {
        List<DepartmentMember> departmentMembers = new ArrayList<>();
        for (Integer analystMemberId : analystMembersId) {
            if (getLastActiveOptionalDepartmentMemberByUserId(analystMemberId).isPresent())
                throw new UserAlreadyDepartmentMemberException(analystMemberId);
            User analystMember = getOptionalUserById(analystMemberId)
                    .orElseThrow(() -> new UserNotFoundException(
                            String.format("No se pudo encontrar un Analista con Id: %d", analystMemberId)
                    ));
            DepartmentMember newDepartmentMember = new DepartmentMember();
            newDepartmentMember.setIsDepartmentBoss(false);
            newDepartmentMember.setAssignmentTimestamp(TimeUtility.getCurrentTimestamp());
            newDepartmentMember.setIsActive(true);
            newDepartmentMember.setUserId(analystMember.getId());
            newDepartmentMember.setDepartmentId(departmentId);
            departmentMembers.add(newDepartmentMember);
        }
        return departmentMemberRepository.saveAll(departmentMembers);
    }

    private List<User> getUsersByIds(List<Integer> usersId) {
        return userRepository.findAllById(usersId);
    }

    private User getNewDepartmentBossOrFail(Integer newDepartmentBossId, Integer departmentId) throws
            InvalidDepartmentBossException, UserNotFoundException {
        verifyIfDepartmentBossIdIsNull(newDepartmentBossId);
        Optional<DepartmentMember> optionalDepartmentMember =
                getLastActiveOptionalDepartmentMemberByUserId(newDepartmentBossId);
        if (optionalDepartmentMember.isPresent()) {
            DepartmentMember departmentMember = optionalDepartmentMember.get();
            if (!departmentMember.getDepartmentId().equals(departmentId) && departmentMember.getIsDepartmentBoss())
                throw new InvalidDepartmentBossException("El usuario ya es Jefe de un Departamento.");
        }
        return getDepartmentBossByDepartmentBossId(newDepartmentBossId);
    }

    private User getOldDepartmentBossByDepartmentId(Integer departmentId) throws DepartmentMemberNotFoundException,
            UserNotFoundException {
        String departmentBossNotFoundMessage = "Jefe del Departamento no encontrado.";
        DepartmentMember departmentBossMember =
                departmentMemberRepository.findDepartmentBossMemberByDepartmentId(departmentId)
                        .orElseThrow(() -> new DepartmentMemberNotFoundException(departmentBossNotFoundMessage));
        User departmentBoss = departmentBossMember.getUser();
        if (departmentBoss == null) throw new UserNotFoundException(departmentBossNotFoundMessage);
        return departmentBoss;
    }

    private DepartmentMember updateDepartmentBossIfNecessary(Integer newDepartmentBossId, Integer oldDepartmentBossId,
                                                             Integer departmentId) throws DepartmentMemberNotFoundException {
        DepartmentMember oldDepartmentBossMember = getLastActiveOptionalDepartmentMemberByUserId(oldDepartmentBossId)
                .orElseThrow(() -> new DepartmentMemberNotFoundException(
                        "Antiguo Miembro Jefe de Departamento no encontrado."
                ));
        if (newDepartmentBossId.equals(oldDepartmentBossId)) return oldDepartmentBossMember;
        oldDepartmentBossMember.setIsActive(false);
        departmentMemberRepository.saveAndFlush(oldDepartmentBossMember);

        return registerNewDepartmentBoss(newDepartmentBossId, departmentId);
    }

    private void deactivateAnalystMembers(List<DepartmentMember> analystMembersToDeactivate) {
        analystMembersToDeactivate.forEach(analystMemberToDeactivate -> {
            analystMemberToDeactivate.setIsActive(false);
            departmentMemberRepository.saveAndFlush(analystMemberToDeactivate);
        });
    }

    private void fillListWithDepartments(List<Department> departments, Department department) {
        departments.add(department);
        List<Department> subordinateDepartments = department.getSubordinateDepartments();
        if (subordinateDepartments != null && !subordinateDepartments.isEmpty())
            subordinateDepartments.forEach(subordinateDepartment ->
                    fillListWithDepartments(departments, subordinateDepartment));
    }

}