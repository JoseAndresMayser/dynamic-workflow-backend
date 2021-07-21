package bo.dynamicworkflow.dynamicworkflowbackend.app.repositories;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DepartmentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentMemberRepository extends JpaRepository<DepartmentMember, Integer> {

    @Query(
            value = "SELECT * FROM department_members dm WHERE dm.user_id = :userId AND dm.is_active IS TRUE LIMIT 1",
            nativeQuery = true
    )
    Optional<DepartmentMember> findLastActiveAssignmentByUserId(@Param("userId") Integer userId);

    @Query(
            value = "SELECT * " +
                    "FROM department_members dm " +
                    "WHERE dm.department_id = :departmentId AND dm.is_department_boss IS TRUE AND " +
                    "dm.is_active IS TRUE LIMIT 1",
            nativeQuery = true
    )
    Optional<DepartmentMember> findDepartmentBossMemberByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query(
            value = "SELECT * " +
                    "FROM department_members dm " +
                    "WHERE dm.department_id = :departmentId AND dm.is_department_boss IS FALSE AND " +
                    "dm.is_active IS TRUE",
            nativeQuery = true
    )
    List<DepartmentMember> getAllActiveAnalystMembersByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query(
            value = "SELECT * FROM department_members dm WHERE dm.is_department_boss IS TRUE AND dm.is_active IS TRUE",
            nativeQuery = true
    )
    List<DepartmentMember> getAllActiveDepartmentBossMembers();

    @Query(value = "SELECT * FROM department_members dm WHERE dm.is_active IS TRUE", nativeQuery = true)
    List<DepartmentMember> getAllActiveDepartmentMembers();

}