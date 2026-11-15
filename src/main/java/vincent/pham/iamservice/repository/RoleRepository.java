package vincent.pham.iamservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vincent.pham.iamservice.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT role
              FROM RoleEntity role
             WHERE LOWER(role.code) = LOWER(:code)
               AND role.isDeleted = false
            """)
    Optional<RoleEntity> findByCodeForUpdate(@Param("code") String code);

    @Query(value = """
            SELECT DISTINCT r.*
            FROM users u
            JOIN user_roles ur
              ON ur.user_id = u.id
             AND ur.is_deleted = FALSE
            JOIN roles r
              ON r.id = ur.role_id
             AND r.is_deleted = FALSE
             AND r.status = 'ACTIVE'
            WHERE u.id = :userId
              AND u.status = 'ACTIVE'
              AND u.is_deleted = FALSE
            ORDER BY r.code
            """, nativeQuery = true)
    List<RoleEntity> findActiveRolesByUserId(@Param("userId") UUID userId);
}
