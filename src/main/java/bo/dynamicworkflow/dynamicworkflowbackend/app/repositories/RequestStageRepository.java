package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.RequestStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestStageRepository extends JpaRepository<RequestStage, Integer> {

    List<RequestStage> getAllByStageId(Integer stageId);

}