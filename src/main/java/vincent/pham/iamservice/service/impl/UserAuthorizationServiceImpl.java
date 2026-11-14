package vincent.pham.iamservice.service.impl;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vincent.pham.iamservice.entity.PermissionEntity;
import vincent.pham.iamservice.entity.RoleEntity;
import vincent.pham.iamservice.repository.PermissionRepository;
import vincent.pham.iamservice.repository.RoleRepository;
import vincent.pham.iamservice.security.model.UserAuthorization;
import vincent.pham.iamservice.service.UserAuthorizationService;

@Service
@RequiredArgsConstructor
public class UserAuthorizationServiceImpl implements UserAuthorizationService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public UserAuthorization getByUserId(UUID userId) {
        Set<String> roles = roleRepository.findActiveRolesByUserId(userId)
                .stream()
                .map(RoleEntity::getCode)
                .collect(Collectors.toUnmodifiableSet());

        Set<String> permissions = permissionRepository.findActivePermissionsByUserId(userId)
                .stream()
                .map(PermissionEntity::getCode)
                .collect(Collectors.toUnmodifiableSet());

        return new UserAuthorization(roles, permissions);
    }
}
