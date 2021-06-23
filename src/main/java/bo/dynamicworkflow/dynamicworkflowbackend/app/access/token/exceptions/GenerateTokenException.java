package bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.exceptions;

public class GenerateTokenException extends Exception{

    public GenerateTokenException(String message) {
        super(message);
    }

    public GenerateTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}