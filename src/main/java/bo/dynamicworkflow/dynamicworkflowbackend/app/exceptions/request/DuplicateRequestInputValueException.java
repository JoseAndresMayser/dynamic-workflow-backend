package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request;

public class DuplicateRequestInputValueException extends RequestException {

    public DuplicateRequestInputValueException() {
        this("Ya se encuentra un valor registrado para la entrada de formulario indicada.");
    }

    public DuplicateRequestInputValueException(String message) {
        super(message);
    }

}