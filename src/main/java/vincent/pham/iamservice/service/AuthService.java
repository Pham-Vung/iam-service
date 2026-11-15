package vincent.pham.iamservice.service;

import vincent.pham.iamservice.dto.request.LoginRequest;
import vincent.pham.iamservice.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
