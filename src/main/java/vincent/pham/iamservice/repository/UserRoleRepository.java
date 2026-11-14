package vincent.pham.iamservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import vincent.pham.iamservice.entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {

    Optional<UserRoleEntity> findByIdAndIsDeletedFalse(UUID id);

    Optional<UserRoleEntity> findByUserIdAndRoleIdAndIsDeletedFalse(
            UUID userId,
            UUID roleId
    );

    boolean existsByUserIdAndRoleIdAndIsDeletedFalse(UUID userId, UUID roleId);

    List<UserRoleEntity> findAllByUserIdAndIsDeletedFalse(UUID userId);

    List<UserRoleEntity> findAllByRoleIdAndIsDeletedFalse(UUID roleId);
}
