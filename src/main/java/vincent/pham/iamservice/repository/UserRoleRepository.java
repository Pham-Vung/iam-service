package vincent.pham.iamservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vincent.pham.iamservice.entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                  FROM users u
                  JOIN user_roles ur
                    ON ur.user_id = u.id
                   AND ur.is_deleted = FALSE
                  JOIN roles r
                    ON r.id = ur.role_id
                   AND r.is_deleted = FALSE
                 WHERE LOWER(r.code) = LOWER(:roleCode)
                   AND r.status = 'ACTIVE'
                   AND u.status = 'ACTIVE'
                   AND u.is_deleted = FALSE
            )
            """, nativeQuery = true)
    boolean existsActiveUserByRoleCode(@Param("roleCode") String roleCode);
}
