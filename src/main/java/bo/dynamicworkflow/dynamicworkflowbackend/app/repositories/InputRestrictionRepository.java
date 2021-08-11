package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.InputRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InputRestrictionRepository extends JpaRepository<InputRestriction, Integer> {

    List<InputRestriction> getAllByInputId(Integer inputId);

}