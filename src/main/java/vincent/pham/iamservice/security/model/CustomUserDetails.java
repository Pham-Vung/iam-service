package vincent.pham.iamservice.security.model;

import java.io.Serial;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vincent.pham.iamservice.enums.UserStatus;

public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String ROLE_PREFIX = "ROLE_";

    private final UUID userId;
    private final String username;
    private final String password;
    private final UserStatus status;
    private final Set<String> roles;
    private final Set<String> permissions;

    public CustomUserDetails(
            UUID userId,
            String username,
            String password,
            UserStatus status,
            Collection<String> roles,
            Collection<String> permissions
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = normalizeValues(roles);
        this.permissions = normalizeValues(permissions);
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        Stream<GrantedAuthority> roleAuthorities = roles.stream()
                .map(role -> role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role)
                .map(SimpleGrantedAuthority::new);

        Stream<GrantedAuthority> permissionAuthorities = permissions.stream()
                .map(SimpleGrantedAuthority::new);

        return Stream.concat(roleAuthorities, permissionAuthorities)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }

    private Set<String> normalizeValues(Collection<String> values) {
        if (values == null) {
            return Set.of();
        }

        return values.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(value -> value.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toUnmodifiableSet());
    }
}
