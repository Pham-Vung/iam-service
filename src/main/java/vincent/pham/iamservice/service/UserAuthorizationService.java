package vincent.pham.iamservice.service;

import java.util.UUID;

import vincent.pham.iamservice.security.model.UserAuthorization;

public interface UserAuthorizationService {

    UserAuthorization getByUserId(UUID userId);
}
