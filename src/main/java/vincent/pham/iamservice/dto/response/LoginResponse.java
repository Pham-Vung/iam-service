package vincent.pham.iamservice.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}
