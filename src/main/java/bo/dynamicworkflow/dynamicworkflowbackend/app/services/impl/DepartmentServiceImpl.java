package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.*;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.user.UserNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Department;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DepartmentMember;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.DepartmentStatus;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.DepartmentMemberRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.DepartmentRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.UserRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.DepartmentService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.CompleteDepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DepartmentRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.CompleteDepartmentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final DepartmentMemberRepository departmentMemberRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, UserRepository userRepository,
                                 DepartmentMemberRepository departmentMemberRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.departmentMemberRepository = departmentMemberRepository;
    }

    @Override
    public CompleteDepartmentResponseDto registerDepartment(CompleteDepartmentRequestDto request)
            throws DepartmentException, UserException {
        DepartmentRequestDto departmentRequestDto = request.getDepartment();
        Integer parentDepartmentId = departmentRequestDto.getParentDepartmentId();
        verifyParentDepartment(parentDepartmentId);
        if (parentDepartmentId != null) {
            verifyDepartmentName(departmentRequestDto.getName(), parentDepartmentId);
        }
        DepartmentStatus status = getDepartmentStatus(departmentRequestDto.getStatus());
        Integer departmentBossId = request.getDepartmentBossId();
        verifyDepartmentBoss(departmentBossId);
        return null;
    }

    private DepartmentStatus getDepartmentStatus(String departmentStatus) throws DepartmentStatusException {
        try {
            if (departmentStatus != null) return DepartmentStatus.valueOf(departmentStatus);
            throw new DepartmentStatusException("El Estado del Departamento no debe ser nulo.");
        } catch (IllegalArgumentException exception) {
            throw new DepartmentStatusException();
        }
    }

    private void verifyParentDepartment(Integer parentDepartmentId) throws DepartmentException {
        if (parentDepartmentId == null) {
            if (departmentRepository.findRootDepartment().isPresent()) throw new RootDepartmentAlreadyExistsException();
            return;
        }
        if (!getOptionalDepartmentById(parentDepartmentId).isPresent())
            throw new DepartmentNotFoundException(
                    String.format("No se pudo encontrar el Departamento padre con Id: %d.", parentDepartmentId)
            );
    }

    private Optional<Department> getOptionalDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId);
    }

    private void verifyDepartmentName(String departmentName, Integer parentDepartmentId) throws DepartmentException {
        if (departmentName == null)
            throw new InvalidDepartmentNameException("El Nombre del Departamento no debe ser nulo.");
        List<Department> subordinateDepartments = departmentRepository.getAllByParentDepartmentId(parentDepartmentId);
        for (Department subordinateDepartment : subordinateDepartments)
            if (subordinateDepartment.getName().equalsIgnoreCase(departmentName))
                throw new DepartmentAlreadyExistsException();
    }

    private void verifyDepartmentBoss(Integer departmentBossId) throws UserNotFoundException {
        if (!userRepository.findById(departmentBossId).isPresent()) {
            throw new UserNotFoundException(
                    String.format(
                            "No se pudo encontrar el Jefe de Departamento a asignar con Id: %d.",
                            departmentBossId
                    )
            );
        }
        List<DepartmentMember> departmentMembers = departmentMemberRepository.getAllByUserId(departmentBossId);
        for (DepartmentMember departmentMember : departmentMembers) {

        }
    }

}