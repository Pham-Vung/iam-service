package vincent.pham.iamservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
