package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DeleteFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.ForbiddenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department.DepartmentNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.departmentmember.DepartmentMemberNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.DigitalCertificateNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate.InvalidCertificateFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Department;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DepartmentMember;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DigitalCertificate;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.DepartmentMemberRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.DepartmentRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.DigitalCertificateRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.DepartmentMemberService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.DepartmentMemberDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.DigitalCertificateRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.FileRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DigitalCertificateResponseDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.DepartmentMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.DepartmentMemberMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.DigitalCertificateMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.UserMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.shared.DepartmentHandler;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.TimeUtility;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver.DigitalCertificateFileManager;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver.SaveFileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentMemberServiceImpl implements DepartmentMemberService {

    private final DepartmentMemberRepository departmentMemberRepository;
    private final DigitalCertificateRepository digitalCertificateRepository;
    private final DepartmentRepository departmentRepository;

    private final DigitalCertificateFileManager digitalCertificateFileManager;

    private final DigitalCertificateMapper digitalCertificateMapper = new DigitalCertificateMapper();
    private final DepartmentMemberMapper departmentMemberMapper = new DepartmentMemberMapper();
    private final UserMapper userMapper = new UserMapper();
    private final DepartmentMapper departmentMapper = new DepartmentMapper();

    @Autowired
    public DepartmentMemberServiceImpl(DepartmentMemberRepository departmentMemberRepository,
                                       DigitalCertificateRepository digitalCertificateRepository,
                                       DepartmentRepository departmentRepository,
                                       DigitalCertificateFileManager digitalCertificateFileManager) {
        this.departmentMemberRepository = departmentMemberRepository;
        this.digitalCertificateRepository = digitalCertificateRepository;
        this.departmentRepository = departmentRepository;
        this.digitalCertificateFileManager = digitalCertificateFileManager;
    }

    @Override
    public DigitalCertificateResponseDto uploadDigitalCertificate(DigitalCertificateRequestDto request,
                                                                  Integer departmentMemberId)
            throws DepartmentMemberNotFoundException, ForbiddenException, InvalidCertificateFileException,
            DirectoryCreationException, SaveFileException, DeleteFileException {
        DepartmentMember departmentMember = getDepartmentMemberOrFail(departmentMemberId);
        Optional<DigitalCertificate> optionalDigitalCertificate =
                digitalCertificateRepository.findByDepartmentMemberId(departmentMember.getId());
        FileRequestDto certificateFile = request.getCertificate();
        verifyCertificateFile(certificateFile);
        DigitalCertificate digitalCertificate;
        if (!optionalDigitalCertificate.isPresent()) {
            digitalCertificate = saveNewDigitalCertificate(departmentMember, certificateFile);
        } else {
            digitalCertificate =
                    updateDigitalCertificate(optionalDigitalCertificate.get(), departmentMember, certificateFile);
        }
        return digitalCertificateMapper.toDto(digitalCertificate);
    }

    @Override
    public List<DepartmentMemberDto> getAllDepartmentMembersByDepartmentId(Integer departmentId)
            throws DepartmentNotFoundException {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        List<DepartmentMember> departmentMembers =
                departmentMemberRepository.getAllActiveDepartmentMembersByDepartmentId(department.getId());
        List<DepartmentMemberDto> response = new ArrayList<>();
        departmentMembers.forEach(departmentMember -> {
            DepartmentMemberDto departmentMemberDto = departmentMemberMapper.toDto(departmentMember);
            departmentMemberDto.setUser(userMapper.toDto(departmentMember.getUser()));
            departmentMemberDto.setDepartment(departmentMapper.toDto(departmentMember.getDepartment()));
            response.add(departmentMemberDto);
        });
        return response;
    }

    @Override
    public DigitalCertificateResponseDto getDigitalCertificateByDepartmentMemberId(Integer departmentMemberId)
            throws DepartmentMemberNotFoundException, ForbiddenException, DigitalCertificateNotFoundException {
        DepartmentMember departmentMember = getDepartmentMemberOrFail(departmentMemberId);
        DigitalCertificate digitalCertificate =
                digitalCertificateRepository.findByDepartmentMemberId(departmentMember.getId())
                        .orElseThrow(() -> new DigitalCertificateNotFoundException(
                                "El Miembro de Departamento no tiene su certificado digital configurado."
                        ));
        return digitalCertificateMapper.toDto(digitalCertificate);
    }

    private DepartmentMember getDepartmentMemberOrFail(Integer departmentMemberId)
            throws DepartmentMemberNotFoundException, ForbiddenException {
        DepartmentMember departmentMember = departmentMemberRepository.findById(departmentMemberId)
                .orElseThrow(() -> new DepartmentMemberNotFoundException(departmentMemberId));
        if (!departmentMember.getUserId().equals(SessionHolder.getCurrentUserId()))
            verifyPermissionForCurrentUser(departmentMember.getDepartmentId());
        return departmentMember;
    }

    private void verifyPermissionForCurrentUser(Integer departmentId) throws DepartmentMemberNotFoundException,
            ForbiddenException {
        DepartmentMember currentDepartmentMember =
                departmentMemberRepository.findLastActiveAssignmentByUserId(SessionHolder.getCurrentUserId())
                        .orElseThrow(() -> new DepartmentMemberNotFoundException(
                                "El usuario actual no es Miembro activo de ningún Departamento."
                        ));
        if (!DepartmentHandler.hasPermitsInDepartment(currentDepartmentMember.getDepartment(), departmentId))
            throw new ForbiddenException(
                    "El usuario actual no tiene permisos sobre el Miembro de Departamento indicado."
            );
    }

    private void verifyCertificateFile(FileRequestDto certificateFile) throws InvalidCertificateFileException {
        if (certificateFile == null)
            throw new InvalidCertificateFileException("El archivo de Certificado Digital no debe ser nulo.");
        if (certificateFile.getFileContent() == null)
            throw new InvalidCertificateFileException(
                    "El contenido del archivo de Certificado Digital no debe ser nulo."
            );
        if (certificateFile.getExtension() == null)
            throw new InvalidCertificateFileException(
                    "La extensión del archivo de Certificado Digital no debe ser nulo."
            );
    }

    private DigitalCertificate saveNewDigitalCertificate(DepartmentMember departmentMember,
                                                         FileRequestDto certificateFile)
            throws DirectoryCreationException, SaveFileException {
        String digitalCertificatePath = saveDigitalCertificateFile(departmentMember, certificateFile);
        Timestamp currentTimestamp = TimeUtility.getCurrentTimestamp();
        DigitalCertificate newDigitalCertificate = new DigitalCertificate();
        newDigitalCertificate.setPath(digitalCertificatePath);
        newDigitalCertificate.setUploadTimestamp(currentTimestamp);
        newDigitalCertificate.setModificationTimestamp(currentTimestamp);
        newDigitalCertificate.setDepartmentMemberId(departmentMember.getId());
        return digitalCertificateRepository.saveAndFlush(newDigitalCertificate);
    }

    private String saveDigitalCertificateFile(DepartmentMember departmentMember, FileRequestDto certificateFile)
            throws DirectoryCreationException, SaveFileException {
        SaveFileRequest digitalCertificateFileRequest = new SaveFileRequest(
                "certificado-digital-" + departmentMember.getUser().getUsername(),
                certificateFile.getFileContent(),
                certificateFile.getExtension()
        );
        return digitalCertificateFileManager.saveDigitalCertificateFile(
                departmentMember.getId(),
                digitalCertificateFileRequest
        );
    }

    private DigitalCertificate updateDigitalCertificate(DigitalCertificate digitalCertificateToUpdate,
                                                        DepartmentMember departmentMember,
                                                        FileRequestDto certificateFile) throws DeleteFileException,
            DirectoryCreationException, SaveFileException {
        digitalCertificateFileManager.deleteFile(digitalCertificateToUpdate.getPath());
        String digitalCertificatePath = saveDigitalCertificateFile(departmentMember, certificateFile);
        digitalCertificateToUpdate.setPath(digitalCertificatePath);
        digitalCertificateToUpdate.setModificationTimestamp(TimeUtility.getCurrentTimestamp());
        return digitalCertificateRepository.saveAndFlush(digitalCertificateToUpdate);
    }

}