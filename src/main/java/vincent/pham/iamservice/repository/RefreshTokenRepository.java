package vincent.pham.iamservice.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vincent.pham.iamservice.entity.RefreshTokenEntity;
import vincent.pham.iamservice.enums.RefreshTokenRevocationReason;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT token
              FROM RefreshTokenEntity token
             WHERE token.tokenHash = :tokenHash
            """)
    Optional<RefreshTokenEntity> findByTokenHashForUpdate(
            @Param("tokenHash") String tokenHash
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE RefreshTokenEntity token
               SET token.revokedAt = :revokedAt,
                   token.revokedReason = :reason,
                   token.updatedDate = :revokedAt
             WHERE token.id = :tokenId
               AND token.revokedAt IS NULL
            """)
    int revokeById(
            @Param("tokenId") UUID tokenId,
            @Param("revokedAt") Instant revokedAt,
            @Param("reason") RefreshTokenRevocationReason reason
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE RefreshTokenEntity token
               SET token.revokedAt = :revokedAt,
                   token.revokedReason = :reason,
                   token.updatedDate = :revokedAt
             WHERE token.familyId = :familyId
               AND token.revokedAt IS NULL
            """)
    int revokeAllByFamilyId(
            @Param("familyId") UUID familyId,
            @Param("revokedAt") Instant revokedAt,
            @Param("reason") RefreshTokenRevocationReason reason
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE RefreshTokenEntity token
               SET token.revokedAt = :revokedAt,
                   token.revokedReason = :reason,
                   token.updatedDate = :revokedAt
             WHERE token.userId = :userId
               AND token.revokedAt IS NULL
            """)
    int revokeAllByUserId(
            @Param("userId") UUID userId,
            @Param("revokedAt") Instant revokedAt,
            @Param("reason") RefreshTokenRevocationReason reason
    );
}
