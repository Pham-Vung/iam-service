package vincent.pham.iamservice.security.token;

import vincent.pham.iamservice.security.model.AccessToken;
import vincent.pham.iamservice.security.model.AccessTokenClaims;
import vincent.pham.iamservice.security.model.CustomUserDetails;

public interface JwtTokenService {

    AccessToken generateAccessToken(CustomUserDetails principal);

    AccessTokenClaims parseAndValidate(String token);
}
