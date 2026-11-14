package vincent.pham.iamservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vincent.pham.iamservice.entity.RoleEntity;
import vincent.pham.iamservice.enums.RoleStatus;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByIdAndIsDeletedFalse(UUID id);

    Optional<RoleEntity> findByCodeIgnoreCaseAndIsDeletedFalse(String code);

    boolean existsByCodeIgnoreCaseAndIsDeletedFalse(String code);

    List<RoleEntity> findAllByStatusAndIsDeletedFalseOrderByNameAsc(RoleStatus status);

    List<RoleEntity> findAllByIdInAndStatusAndIsDeletedFalse(
            List<UUID> ids,
            RoleStatus status
    );

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
