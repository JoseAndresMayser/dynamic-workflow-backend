package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.ProcessSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessSchemaRepository extends JpaRepository<ProcessSchema, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM process_schemas ps " +
                    "WHERE ps.process_id = :processId AND ps.is_active IS TRUE LIMIT 1",
            nativeQuery = true
    )
    Optional<ProcessSchema> findLastActiveByProcessId(@Param("processId") Integer processId);

}