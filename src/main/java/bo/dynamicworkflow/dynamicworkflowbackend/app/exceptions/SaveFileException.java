package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions;

public class SaveFileException extends Exception {

    public SaveFileException(String message) {
        super(message);
    }

    public SaveFileException(String message, Throwable cause) {
        super(message, cause);
    }

}