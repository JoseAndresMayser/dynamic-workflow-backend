package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {

    private PasswordEncryptor() {
        throw new IllegalStateException("Can't instantiate this class");
    }

    public static String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    public static Boolean checkPassword(String plainTextPassword, String storedHash) {
        if (storedHash == null || !storedHash.startsWith("$2a$"))
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        return BCrypt.checkpw(plainTextPassword, storedHash);
    }

    private static final Integer WORKLOAD = 12;

}