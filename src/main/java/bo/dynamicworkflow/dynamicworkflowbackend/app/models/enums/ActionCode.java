package bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum ActionCode {

    NOT_MAIN_ACTION,

    ACCESS_LOG_IN,
    ACCESS_PASSWORD_RESTORE,
    ACCESS_PASSWORD_UPDATE,

    ROLE_REGISTER,
    ROLE_UPDATE,
    ROLE_GET_ALL,
    ROLE_DELETE,

    USER_REGISTER_REQUESTING,
    USER_REGISTER,
    USER_UPDATE,
    USER_CURRENT_UPDATE,
    USER_GET_ALL,

    DEPARTMENT_REGISTER,
    DEPARTMENT_UPDATE,
    DEPARTMENT_UPDATE_MEMBERS,
    DEPARTMENT_GET,
    DEPARTMENT_GET_ALL,

    PROCESS_CREATE,
    PROCESS_GET_ALL,

    REQUEST_CREATE;

    public static List<ActionCode> actionsWithoutAuth() {
        return new ArrayList<ActionCode>() {
            {
                add(ACCESS_LOG_IN);
                add(ACCESS_PASSWORD_RESTORE);
                add(USER_REGISTER_REQUESTING);
            }
        };
    }

}