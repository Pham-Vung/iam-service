package vincent.pham.iamservice.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vincent.pham.iamservice.entity.UserEntity;
import vincent.pham.iamservice.repository.UserRepository;
import vincent.pham.iamservice.security.model.CustomUserDetails;
import vincent.pham.iamservice.security.model.UserAuthorization;
import vincent.pham.iamservice.service.UserAuthorizationService;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserAuthorizationService userAuthorizationService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizedUsername = normalizeUsername(username);

        UserEntity user = userRepository
                .findByUsernameIgnoreCaseAndIsDeletedFalse(normalizedUsername)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Tên đăng nhập hoặc mật khẩu không hợp lệ"
                ));

        UserAuthorization authorization = userAuthorizationService.getByUserId(user.getId());

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getStatus(),
                authorization.roles(),
                authorization.permissions()
        );
    }

    private String normalizeUsername(String username) {
        return username == null ? "" : username.trim();
    }
}
