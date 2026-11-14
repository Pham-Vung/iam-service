package vincent.pham.iamservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import vincent.pham.iamservice.entity.UserEntity;
import vincent.pham.iamservice.enums.UserStatus;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByIdAndIsDeletedFalse(UUID id);

    Optional<UserEntity> findByUsernameIgnoreCaseAndIsDeletedFalse(String username);

    Optional<UserEntity> findByUsernameIgnoreCaseAndStatusAndIsDeletedFalse(
            String username,
            UserStatus status
    );

    Optional<UserEntity> findByEmailIgnoreCaseAndIsDeletedFalse(String email);

    boolean existsByUsernameIgnoreCaseAndIsDeletedFalse(String username);

    boolean existsByEmailIgnoreCaseAndIsDeletedFalse(String email);
}
