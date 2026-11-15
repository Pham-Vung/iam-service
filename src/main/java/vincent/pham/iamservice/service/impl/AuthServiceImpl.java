package vincent.pham.iamservice.service.impl;

import java.time.Instant;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vincent.pham.iamservice.repository.UserRepository;
import vincent.pham.iamservice.dto.request.LoginRequest;
import vincent.pham.iamservice.dto.response.LoginResponse;
import vincent.pham.iamservice.security.model.AccessToken;
import vincent.pham.iamservice.security.model.CustomUserDetails;
import vincent.pham.iamservice.security.model.IssuedRefreshToken;
import vincent.pham.iamservice.security.token.JwtTokenService;
import vincent.pham.iamservice.security.token.refresh.RefreshTokenService;
import vincent.pham.iamservice.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.username().trim(),
                        request.password()
                )
        );

        if (!(authentication.getPrincipal() instanceof CustomUserDetails principal)) {
            throw new AuthenticationServiceException(
                    "Không hỗ trợ thông tin người dùng đã xác thực"
            );
        }

        AccessToken accessToken = jwtTokenService.generateAccessToken(principal);
        IssuedRefreshToken refreshToken = refreshTokenService.issue(
                principal.getUserId(),
                UUID.randomUUID()
        );

        int updatedUsers = userRepository.updateLastLoginAt(
                principal.getUserId(),
                Instant.now()
        );
        if (updatedUsers != 1) {
            throw new AuthenticationServiceException(
                    "Người dùng đã xác thực không còn tồn tại"
            );
        }

        return new LoginResponse(
                accessToken.value(),
                refreshToken.value(),
                accessToken.expiresIn()
        );
    }
}
