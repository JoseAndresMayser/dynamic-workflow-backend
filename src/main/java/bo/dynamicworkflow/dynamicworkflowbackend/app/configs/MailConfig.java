package bo.dynamicworkflow.dynamicworkflowbackend.app.configs;

import java.util.Properties;

public class MailConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties")
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final String MAIL_SMTP_SERVER = (String) PROPERTIES.get("mail.smtp.server");
    public static final String MAIL_SMTP_USERNAME = (String) PROPERTIES.get("mail.smtp.username");
    public static final String MAIL_SMTP_PASSWORD = (String) PROPERTIES.get("mail.smtp.password");
    public static final String MAIL_SMTP_SENDER = (String) PROPERTIES.get("mail.smtp.sender");
    public static final String MAIL_SMTP_SENDER_NAME = (String) PROPERTIES.get("mail.smtp.sender.name");
    public static final Integer MAIL_SMTP_PORT = Integer.parseInt(PROPERTIES.getProperty("mail.smtp.port"));
    public static final String MAIL_SMTP_PORT_SSL = PROPERTIES.getProperty("mail.smtp.port.ssl");
    public static final Boolean MAIL_SMTP_SSL = Boolean.parseBoolean(PROPERTIES.getProperty("mail.smtp.ssl"));

}