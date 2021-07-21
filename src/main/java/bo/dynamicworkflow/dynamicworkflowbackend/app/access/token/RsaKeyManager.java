package bo.dynamicworkflow.dynamicworkflowbackend.app.access.token;

import bo.dynamicworkflow.dynamicworkflowbackend.app.configs.RsaKeyConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private final String privateKeyFilePath;
    private final String publicKeyFilePath;

    private static final String ALGORITHM = "RSA";

    @Autowired
    public RsaKeyManager(RsaKeyConfig rsaKeyConfig) throws Exception {
        this.privateKeyFilePath = rsaKeyConfig.getPrivateKeyPath();
        this.publicKeyFilePath = rsaKeyConfig.getPublicKeyPath();
        initialize();
    }

    private void initialize() throws Exception {
        if (!areKeysPresent()) generateKeys();
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(privateKeyFilePath));
        privateKey = (PrivateKey) inputStream.readObject();
        inputStream = new ObjectInputStream(new FileInputStream(publicKeyFilePath));
        publicKey = (PublicKey) inputStream.readObject();
    }

    private boolean areKeysPresent() {
        File privateKey = new File(privateKeyFilePath);
        File publicKey = new File(publicKeyFilePath);
        return privateKey.exists() && publicKey.exists();
    }

    private void generateKeys() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(2048);
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

            File privateKeyFile = new File(privateKeyFilePath);
            File publicKeyFile = new File(publicKeyFilePath);

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

}