package vincent.pham.iamservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vincent.pham.iamservice.entity.PermissionEntity;
import vincent.pham.iamservice.enums.PermissionStatus;

public interface PermissionRepository extends JpaRepository<PermissionEntity, UUID> {

    Optional<PermissionEntity> findByIdAndIsDeletedFalse(UUID id);

    Optional<PermissionEntity> findByCodeIgnoreCaseAndIsDeletedFalse(String code);

    boolean existsByCodeIgnoreCaseAndIsDeletedFalse(String code);

    boolean existsByModuleIgnoreCaseAndActionIgnoreCaseAndIsDeletedFalse(
            String module,
            String action
    );

    List<PermissionEntity> findAllByStatusAndIsDeletedFalseOrderByModuleAscActionAsc(
            PermissionStatus status
    );

    List<PermissionEntity> findAllByIdInAndStatusAndIsDeletedFalse(
            List<UUID> ids,
            PermissionStatus status
    );

    @Query(value = """
            SELECT DISTINCT p.*
            FROM users u
            JOIN user_roles ur
              ON ur.user_id = u.id
             AND ur.is_deleted = FALSE
            JOIN roles r
              ON r.id = ur.role_id
             AND r.status = 'ACTIVE'
             AND r.is_deleted = FALSE
            JOIN role_permissions rp
              ON rp.role_id = r.id
             AND rp.is_deleted = FALSE
            JOIN permissions p
              ON p.id = rp.permission_id
             AND p.status = 'ACTIVE'
             AND p.is_deleted = FALSE
            WHERE u.id = :userId
              AND u.status = 'ACTIVE'
              AND u.is_deleted = FALSE
            ORDER BY p.code
            """, nativeQuery = true)
    List<PermissionEntity> findActivePermissionsByUserId(@Param("userId") UUID userId);
}
