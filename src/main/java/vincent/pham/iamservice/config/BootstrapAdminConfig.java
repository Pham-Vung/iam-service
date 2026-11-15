package vincent.pham.iamservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vincent.pham.iamservice.config.properties.BootstrapAdminProperties;

@Configuration
@EnableConfigurationProperties(BootstrapAdminProperties.class)
public class BootstrapAdminConfig {
}
