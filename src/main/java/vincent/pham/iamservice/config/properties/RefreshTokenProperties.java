package vincent.pham.iamservice.config.properties;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.refresh-token")
public class RefreshTokenProperties {

    @NotNull(message = "Thời hạn refresh token không được để trống")
    private Duration expiration = Duration.ofDays(30);
}
