package bo.dynamicworkflow.dynamicworkflowbackend.app.access.token;

import bo.dynamicworkflow.dynamicworkflowbackend.app.configs.DynamicWorkflowConfig;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RsaKeyManager implements Serializable {

    @Getter
    private PrivateKey privateKey;
    @Getter
    private PublicKey publicKey;

    @PostConstruct
    private void initialize() throws Exception {
        if (!areKeysPresent()) generateKeys();
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE_PATH));
        privateKey = (PrivateKey) inputStream.readObject();
        inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE_PATH));
        publicKey = (PublicKey) inputStream.readObject();
    }

    private boolean areKeysPresent() {
        File privateKey = new File(PRIVATE_KEY_FILE_PATH);
        File publicKey = new File(PUBLIC_KEY_FILE_PATH);
        return privateKey.exists() && publicKey.exists();
    }

    private void generateKeys() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(2048);
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE_PATH);
            File publicKeyFile = new File(PUBLIC_KEY_FILE_PATH);

            if (privateKeyFile.getParentFile() != null) privateKeyFile.getParentFile().mkdirs();
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) publicKeyFile.getParentFile().mkdirs();
            publicKeyFile.createNewFile();

            ObjectOutputStream privateKeyOutput = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
            privateKeyOutput.writeObject(keyPair.getPrivate());
            privateKeyOutput.close();

            ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(keyPair.getPublic());
            publicKeyOS.close();
        } catch (Exception e) {
            System.out.println("Failed to generate key pair: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String ALGORITHM = "RSA";
    private final String PRIVATE_KEY_FILE_PATH = DynamicWorkflowConfig.PRIVATE_KEY_PATH;
    private final String PUBLIC_KEY_FILE_PATH = DynamicWorkflowConfig.PUBLIC_KEY_PATH;

}