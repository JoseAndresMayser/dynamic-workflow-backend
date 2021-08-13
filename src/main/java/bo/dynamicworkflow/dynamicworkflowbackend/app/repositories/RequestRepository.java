package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> getAllByUserId(Integer userId);

}