package vincent.pham.iamservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vincent.pham.iamservice.common.response.ApiResponse;
import vincent.pham.iamservice.dto.request.LoginRequest;
import vincent.pham.iamservice.dto.response.LoginResponse;
import vincent.pham.iamservice.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @SecurityRequirements
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ApiResponse.success(authService.login(request));
    }
}
