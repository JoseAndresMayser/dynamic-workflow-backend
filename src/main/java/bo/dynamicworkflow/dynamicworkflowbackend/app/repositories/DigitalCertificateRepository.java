package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DigitalCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DigitalCertificateRepository extends JpaRepository<DigitalCertificate, Integer> {

    Optional<DigitalCertificate> findByDepartmentMemberId(Integer departmentMemberId);

}