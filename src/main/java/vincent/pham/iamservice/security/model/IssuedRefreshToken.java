package vincent.pham.iamservice.security.model;

import java.time.Instant;
import java.util.UUID;

public record IssuedRefreshToken(
        String value,
        UUID tokenId,
        UUID familyId,
        Instant expiresAt
) {
}
