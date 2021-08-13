package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.StageAnalyst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageAnalystRepository extends JpaRepository<StageAnalyst, Integer> {

    List<StageAnalyst> getAllByStageId(Integer stageId);

    List<StageAnalyst> getAllByDepartmentMemberId(Integer departmentMemberId);

}