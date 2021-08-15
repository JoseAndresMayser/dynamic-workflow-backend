package bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums;

public enum RequestStatus {

    IN_PROCESS,
    APPROVED,
    REJECTED;

    public static String getHumanReadableFormat(RequestStatus requestStatus) {
        switch (requestStatus) {
            case APPROVED:
                return "APROBADA";
            case REJECTED:
                return "RECHAZADA";
        }
        return "";
    }

}