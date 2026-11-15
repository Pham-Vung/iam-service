package vincent.pham.iamservice.security.model;

import java.time.Instant;

public record AccessToken(
        String value,
        Instant issuedAt,
        Instant expiresAt,
        long expiresIn
) {
}
