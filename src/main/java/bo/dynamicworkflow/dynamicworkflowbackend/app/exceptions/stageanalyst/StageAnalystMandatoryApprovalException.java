package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.stageanalyst;

public class StageAnalystMandatoryApprovalException extends StageAnalystException {

    public StageAnalystMandatoryApprovalException() {
        this("Aprobación obligatoria inválida del analista de la etapa.");
    }

    public StageAnalystMandatoryApprovalException(String message) {
        super(message);
    }

}