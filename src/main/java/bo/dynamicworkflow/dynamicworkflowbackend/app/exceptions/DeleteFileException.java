package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions;

public class DeleteFileException extends Exception {

    public DeleteFileException() {
        this("Ocurrió un error al intentar eliminar el archivo.");
    }

    public DeleteFileException(String message) {
        super(message);
    }

}