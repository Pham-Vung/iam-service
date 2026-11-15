package vincent.pham.iamservice.security.token.refresh;

import java.util.UUID;

import vincent.pham.iamservice.security.model.IssuedRefreshToken;

public interface RefreshTokenService {

  IssuedRefreshToken issue(UUID userId, UUID familyId);
}
