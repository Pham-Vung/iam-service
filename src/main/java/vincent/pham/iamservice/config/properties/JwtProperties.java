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

    @NotBlank
    @Size(min = 32)
    private String secret;

    @NotBlank
    private String issuer = "iam-service";

    @NotNull
    private Duration accessTokenExpiration = Duration.ofMinutes(15);
}
