package vincent.pham.iamservice.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vincent.pham.iamservice.config.properties.BootstrapAdminProperties;
import vincent.pham.iamservice.entity.RoleEntity;
import vincent.pham.iamservice.entity.UserEntity;
import vincent.pham.iamservice.entity.UserRoleEntity;
import vincent.pham.iamservice.enums.RoleStatus;
import vincent.pham.iamservice.enums.UserStatus;
import vincent.pham.iamservice.repository.RoleRepository;
import vincent.pham.iamservice.repository.UserRepository;
import vincent.pham.iamservice.repository.UserRoleRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialAdminBootstrap implements ApplicationRunner {

    private static final String SUPER_ADMIN_ROLE_CODE = "SUPER_ADMIN";
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    private final BootstrapAdminProperties properties;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!properties.isEnabled()) {
            return;
        }

        validateConfiguration();

        RoleEntity superAdminRole = roleRepository
                .findByCodeForUpdate(SUPER_ADMIN_ROLE_CODE)
                .orElseThrow(() -> new IllegalStateException(
                        "Không tìm thấy vai trò SUPER_ADMIN"
                ));

        if (superAdminRole.getStatus() != RoleStatus.ACTIVE) {
            throw new IllegalStateException("Vai trò SUPER_ADMIN đang không hoạt động");
        }

        if (userRoleRepository.existsActiveUserByRoleCode(SUPER_ADMIN_ROLE_CODE)) {
            log.info("Hệ thống đã có tài khoản quản trị, bỏ qua bước khởi tạo");
            return;
        }

        String username = properties.getUsername().trim();
        if (userRepository.findByUsernameIgnoreCaseAndIsDeletedFalse(username).isPresent()) {
            throw new IllegalStateException(
                    "Tên đăng nhập cấu hình cho quản trị viên đã tồn tại"
            );
        }

        UserEntity admin = new UserEntity();
        admin.setUsername(username);
        admin.setPasswordHash(passwordEncoder.encode(properties.getPassword()));
        admin.setEmail(normalizeOptionalValue(properties.getEmail()));
        admin.setFullName(normalizeOptionalValue(properties.getFullName()));
        admin.setStatus(UserStatus.ACTIVE);
        UserEntity savedAdmin = userRepository.save(admin);

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(savedAdmin.getId());
        userRole.setRoleId(superAdminRole.getId());
        userRoleRepository.save(userRole);

        log.info("Đã khởi tạo tài khoản quản trị ban đầu: {}", username);
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(properties.getUsername())) {
            throw new IllegalStateException(
                    "Chưa cấu hình tên đăng nhập cho tài khoản quản trị ban đầu"
            );
        }
        if (properties.getUsername().trim().length() > 100) {
            throw new IllegalStateException(
                    "Tên đăng nhập quản trị không được vượt quá 100 ký tự"
            );
        }
        if (!StringUtils.hasText(properties.getPassword())) {
            throw new IllegalStateException(
                    "Chưa cấu hình mật khẩu cho tài khoản quản trị ban đầu"
            );
        }
        if (properties.getPassword().length() < MINIMUM_PASSWORD_LENGTH) {
            throw new IllegalStateException(
                    "Mật khẩu quản trị phải có ít nhất 8 ký tự"
            );
        }
        validateMaximumLength(properties.getEmail(), 255, "Email quản trị");
        validateMaximumLength(properties.getFullName(), 255, "Tên quản trị viên");
    }

    private void validateMaximumLength(String value, int maximumLength, String fieldName) {
        if (value != null && value.trim().length() > maximumLength) {
            throw new IllegalStateException(
                    fieldName + " không được vượt quá " + maximumLength + " ký tự"
            );
        }
    }

    private String normalizeOptionalValue(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
