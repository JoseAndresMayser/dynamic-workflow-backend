package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions;

public class DirectoryCreationException extends Exception {

    public DirectoryCreationException() {
        this("No se pudo crear el directorio para el archivo.");
    }

    public DirectoryCreationException(String message) {
        super(message);
    }

}