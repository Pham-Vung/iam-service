package vincent.pham.iamservice.security.token.refresh;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vincent.pham.iamservice.config.properties.RefreshTokenProperties;
import vincent.pham.iamservice.entity.RefreshTokenEntity;
import vincent.pham.iamservice.repository.RefreshTokenRepository;
import vincent.pham.iamservice.security.model.IssuedRefreshToken;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenHasher refreshTokenHasher;
    private final RefreshTokenProperties refreshTokenProperties;

    @Override
    @Transactional
    public IssuedRefreshToken issue(UUID userId, UUID familyId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID người dùng không được để trống");
        }
        if (familyId == null) {
            throw new IllegalArgumentException("ID nhóm token không được để trống");
        }

        Duration expiration = refreshTokenProperties.getExpiration();
        if (expiration.isZero() || expiration.isNegative()) {
            throw new IllegalStateException("Thời hạn refresh token phải lớn hơn 0");
        }

        String rawToken = refreshTokenGenerator.generate();
        Instant expiresAt = Instant.now().plus(expiration);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUserId(userId);
        refreshToken.setTokenHash(refreshTokenHasher.hash(rawToken));
        refreshToken.setFamilyId(familyId);
        refreshToken.setExpiresAt(expiresAt);

        RefreshTokenEntity savedToken = refreshTokenRepository.save(refreshToken);
        return new IssuedRefreshToken(
                rawToken,
                savedToken.getId(),
                savedToken.getFamilyId(),
                savedToken.getExpiresAt()
        );
    }
}
