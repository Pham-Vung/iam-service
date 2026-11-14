package vincent.pham.iamservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import vincent.pham.iamservice.entity.RolePermissionEntity;

public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, UUID> {

    Optional<RolePermissionEntity> findByIdAndIsDeletedFalse(UUID id);

    Optional<RolePermissionEntity> findByRoleIdAndPermissionIdAndIsDeletedFalse(
            UUID roleId,
            UUID permissionId
    );

    boolean existsByRoleIdAndPermissionIdAndIsDeletedFalse(
            UUID roleId,
            UUID permissionId
    );

    List<RolePermissionEntity> findAllByRoleIdAndIsDeletedFalse(UUID roleId);

    List<RolePermissionEntity> findAllByPermissionIdAndIsDeletedFalse(UUID permissionId);
}
