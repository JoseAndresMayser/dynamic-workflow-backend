package bo.dynamicworkflow.dynamicworkflowbackend.app.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FileStorageConfig {

    @Value("${file.storage.path.base}")
    private String basePath;

    @Value("${file.storage.path.department-members}")
    private String departmentMembersPath;

    @Value("${file.storage.path.requests}")
    private String requestsPath;

}