package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.department;


public class InvalidAnalystMembersIdListException extends DepartmentException {

    public InvalidAnalystMembersIdListException() {
        this("La lista de ID de analistas es inválida.");
    }

    public InvalidAnalystMembersIdListException(String message) {
        super(message);
    }

}