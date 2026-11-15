package vincent.pham.iamservice.security.token;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vincent.pham.iamservice.config.properties.JwtProperties;
import vincent.pham.iamservice.security.model.AccessToken;
import vincent.pham.iamservice.security.model.AccessTokenClaims;
import vincent.pham.iamservice.security.model.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private static final String USER_ID_CLAIM = "userId";
    private static final String ROLES_CLAIM = "roles";
    private static final String PERMISSIONS_CLAIM = "permissions";

    private final JwtProperties jwtProperties;
    private final SecretKey jwtSigningKey;

    @Override
    public AccessToken generateAccessToken(CustomUserDetails principal) {
        Duration expiration = jwtProperties.getAccessTokenExpiration();
        if (expiration.isZero() || expiration.isNegative()) {
            throw new IllegalStateException("Thời hạn access token phải lớn hơn 0");
        }

        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiresAt = issuedAt.plus(expiration);
        String tokenId = UUID.randomUUID().toString();

        String value = Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(principal.getUsername())
                .id(tokenId)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .claim(USER_ID_CLAIM, principal.getUserId().toString())
                .claim(ROLES_CLAIM, principal.getRoles().stream().sorted().toList())
                .claim(PERMISSIONS_CLAIM, principal.getPermissions().stream().sorted().toList())
                .signWith(jwtSigningKey, Jwts.SIG.HS256)
                .compact();

        return new AccessToken(value, issuedAt, expiresAt, expiration.toSeconds());
    }

    @Override
    public AccessTokenClaims parseAndValidate(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("Access token không được để trống");
        }

        Claims claims = Jwts.parser()
                .verifyWith(jwtSigningKey)
                .requireIssuer(jwtProperties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new AccessTokenClaims(
                UUID.fromString(claims.get(USER_ID_CLAIM, String.class)),
                claims.getSubject(),
                getStringSetClaim(claims, ROLES_CLAIM),
                getStringSetClaim(claims, PERMISSIONS_CLAIM),
                claims.getId(),
                claims.getIssuer(),
                claims.getIssuedAt().toInstant(),
                claims.getExpiration().toInstant()
        );
    }

    private Set<String> getStringSetClaim(Claims claims, String claimName) {
        Object claimValue = claims.get(claimName);
        if (!(claimValue instanceof List<?> values)) {
            return Collections.emptySet();
        }

        return values.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .filter(StringUtils::hasText)
                .collect(Collectors.toUnmodifiableSet());
    }
}
