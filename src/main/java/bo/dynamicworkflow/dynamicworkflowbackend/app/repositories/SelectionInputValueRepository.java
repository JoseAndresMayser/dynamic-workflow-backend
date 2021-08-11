package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.SelectionInputValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectionInputValueRepository extends JpaRepository<SelectionInputValue, Integer> {

    List<SelectionInputValue> getAllByInputId(Integer inputId);

}