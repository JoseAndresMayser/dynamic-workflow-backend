package bo.dynamicworkflow.dynamicworkflowbackend.app.configs;

public class SecurityConfig {

    private SecurityConfig() {
        throw new IllegalStateException("Can't instantiate this class");
    }

    public static final String[] FREE_ENDPOINTS = {
            "/api/access/log-in",
            "/api/access/password/restore",
            "/api/users/requesting"
    };

}