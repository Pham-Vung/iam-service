package vincent.pham.iamservice.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vincent.pham.iamservice.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByUsernameIgnoreCaseAndIsDeletedFalse(String username);

  @Modifying(flushAutomatically = true)
  @Query("""
      UPDATE UserEntity user
         SET user.lastLoginAt = :loginTime
       WHERE user.id = :userId
         AND user.isDeleted = false
      """)
  int updateLastLoginAt(
      @Param("userId") UUID userId,
      @Param("loginTime") Instant loginTime
  );
}
