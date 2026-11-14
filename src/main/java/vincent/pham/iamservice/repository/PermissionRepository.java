package vincent.pham.iamservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
