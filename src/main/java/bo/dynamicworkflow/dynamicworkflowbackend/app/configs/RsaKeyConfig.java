package bo.dynamicworkflow.dynamicworkflowbackend.app.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RsaKeyConfig {

    @Value("${rsa.key.private-key-path}")
    private String privateKeyPath;

    @Value("${rsa.key.public-key-path}")
    public String publicKeyPath;

}