package vincent.pham.iamservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Tên đăng nhập không được để trống")
        @Size(max = 100, message = "Tên đăng nhập không được vượt quá 100 ký tự")
        @Schema(
                description = "Tên đăng nhập của tài khoản",
                example = "admin",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String username,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(max = 255, message = "Mật khẩu không được vượt quá 255 ký tự")
        @Schema(
                description = "Mật khẩu của tài khoản",
                example = "P@ssw0rd!",
                format = "password",
                accessMode = Schema.AccessMode.WRITE_ONLY,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String password
) {
}
