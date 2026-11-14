package vincent.pham.iamservice.security.model;

import java.util.Set;

public record UserAuthorization(
        Set<String> roles,
        Set<String> permissions
) {

    public UserAuthorization {
        roles = Set.copyOf(roles);
        permissions = Set.copyOf(permissions);
    }
}
