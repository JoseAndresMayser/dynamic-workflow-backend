package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.processschema;

public class ProcessSchemaNotFoundException extends ProcessSchemaException {

    public ProcessSchemaNotFoundException() {
        this("Esquema de proceso no encontrado.");
    }

    public ProcessSchemaNotFoundException(String message) {
        super(message);
    }

}