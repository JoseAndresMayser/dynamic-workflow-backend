package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities;

import java.util.Random;
import java.util.stream.IntStream;

public final class PasswordGenerator {

    private static final char[] characters = new char[100];
    private static int charactersCount = 0;
    private final int passwordSize;

    public PasswordGenerator(int passwordSize) {
        this.passwordSize = passwordSize;
        initCharacters();
    }

    private static void initCharacters() {
        int i = 0;
        charactersCount = 0;
        for (int j = 48; j < 58; ++i, ++j, ++charactersCount) characters[i] = (char) j;
        for (int j = 64; j < 91; ++i, ++j, ++charactersCount) characters[i] = (char) j;
        for (int j = 97; j < 123; ++i, ++j, ++charactersCount) characters[i] = (char) j;
    }

    public char[] get() {
        Random random = new Random();
        char[] password = new char[passwordSize];
        IntStream.range(0, passwordSize).forEach(
                index -> {
                    int randomInt = random.nextInt(charactersCount);
                    password[index] = characters[randomInt];
                }
        );
        return password;
    }

}