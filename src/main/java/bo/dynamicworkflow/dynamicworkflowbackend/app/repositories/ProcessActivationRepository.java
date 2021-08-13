package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.ProcessActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessActivationRepository extends JpaRepository<ProcessActivation, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM process_activations pa " +
                    "WHERE pa.process_schema_id = :processSchemaId AND pa.is_active IS TRUE LIMIT 1",
            nativeQuery = true
    )
    Optional<ProcessActivation> findLastActiveByProcessSchemaId(@Param("processSchemaId") Integer processSchemaId);

}