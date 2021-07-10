package bo.dynamicworkflow.dynamicworkflowbackend.app.configs;

public class SecurityConfig {

    private SecurityConfig() {
        throw new IllegalStateException("Can't instantiate this class");
    }

    public static final String[] UNPROTECTED_ENDPOINTS = {
            "/rest/access/log-in",
            "/rest/access/password/restore",
            "/rest/users/requesting"
    };

}