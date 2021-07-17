package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DepartmentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMemberRepository extends JpaRepository<DepartmentMember, Integer> {

    List<DepartmentMember> getAllByUserId(Integer userId);

}