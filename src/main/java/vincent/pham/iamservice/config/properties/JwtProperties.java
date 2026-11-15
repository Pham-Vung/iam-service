package vincent.pham.iamservice.config.properties;

import java.time.Duration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    @NotBlank(message = "Khóa bí mật JWT không được để trống")
    @Size(min = 32, message = "Khóa bí mật JWT phải có ít nhất 32 ký tự")
    private String secret;

    @NotBlank(message = "JWT issuer không được để trống")
    private String issuer = "iam-service";

    @NotNull(message = "Thời hạn access token không được để trống")
    private Duration accessTokenExpiration = Duration.ofMinutes(15);
}
