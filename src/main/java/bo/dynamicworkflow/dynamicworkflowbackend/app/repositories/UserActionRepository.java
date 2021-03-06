package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Integer> {

    List<UserAction> getAllByUserId(Integer userId);

}