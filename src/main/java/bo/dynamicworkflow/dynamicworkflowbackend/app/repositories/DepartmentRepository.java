package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Query(value = "SELECT * FROM departments d WHERE d.parent_department_id IS NULL LIMIT 1", nativeQuery = true)
    Optional<Department> findRootDepartment();

    List<Department> getAllByParentDepartmentId(Integer parentDepartmentId);

}