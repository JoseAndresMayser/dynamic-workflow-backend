package bo.dynamicworkflow.dynamicworkflowbackend.app.configs;

import java.util.Properties;

public class DynamicWorkflowConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "dynamic-workflow.properties"
            ));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final String PRIVATE_KEY_PATH = (String) PROPERTIES.get("private-key.path");
    public static final String PUBLIC_KEY_PATH = (String) PROPERTIES.get("public-key.path");

}