package bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum ActionCode {

    ACCESS_LOG_IN,
    ACCESS_PASSWORD_RESTORE,
    ACCESS_PASSWORD_UPDATE,

    ROLE_REGISTER,
    ROLE_UPDATE,
    ROLE_GET,
    ROLE_GET_ALL,
    ROLE_ACTIONS_GET,

    USER_REGISTER_REQUESTING,
    USER_REGISTER,
    USER_UPDATE,
    USER_CURRENT_UPDATE,
    USER_GET,
    USER_CURRENT_GET,
    USER_GET_ALL;

    public static List<ActionCode> getActionsCodeWithoutAuth() {
        return new ArrayList<ActionCode>() {
            {
                add(ACCESS_LOG_IN);
                add(ACCESS_PASSWORD_RESTORE);
                add(USER_REGISTER_REQUESTING);
            }
        };
    }

}