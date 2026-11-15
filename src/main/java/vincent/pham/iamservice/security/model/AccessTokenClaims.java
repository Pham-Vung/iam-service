package vincent.pham.iamservice.security.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AccessTokenClaims(
        UUID userId,
        String username,
        Set<String> roles,
        Set<String> permissions,
        String tokenId,
        String issuer,
        Instant issuedAt,
        Instant expiresAt
) {

    public AccessTokenClaims {
        roles = Set.copyOf(roles);
        permissions = Set.copyOf(permissions);
    }
}
