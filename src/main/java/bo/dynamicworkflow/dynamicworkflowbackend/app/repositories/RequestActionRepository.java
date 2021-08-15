package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.RequestAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestActionRepository extends JpaRepository<RequestAction, Integer> {

    Optional<RequestAction> findByRequestIdAndStageAnalystId(Integer requestId, Integer stageAnalystId);

    List<RequestAction> getAllByRequestId(Integer requestId);

}