package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Process;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Integer> {

    List<Process> getAllByDepartmentId(Integer departmentId);

    List<Process> getAllByDepartmentIdAndStatus(Integer departmentId, ProcessStatus status);

}