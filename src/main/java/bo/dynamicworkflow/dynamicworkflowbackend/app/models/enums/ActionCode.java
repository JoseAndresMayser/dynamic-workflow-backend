package bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums;

import java.util.ArrayList;
import java.util.List;

public enum ActionCode {

    ROLE_REGISTER,
    ROLE_UPDATE,
    ROLE_ACTIONS_UPDATE,
    ROLE_GET,
    ROLE_GET_ALL,
    ROLE_ACTIONS_GET,

    USER_LOG_IN,
    USER_REGISTER,
    USER_REGISTER_REQUESTING,
    USER_PASSWORD_RESTORE,
    USER_PASSWORD_UPDATE;

    public static List<ActionCode> getActionsCodeWithoutAuthentication() {
        return new ArrayList<ActionCode>() {
            {
                add(USER_LOG_IN);
                add(USER_REGISTER_REQUESTING);
                add(USER_PASSWORD_RESTORE);
            }
        };
    }

}