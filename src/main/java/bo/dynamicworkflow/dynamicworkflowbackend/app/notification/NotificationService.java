package bo.dynamicworkflow.dynamicworkflowbackend.app.notification;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;

public interface NotificationService {

    void sendRequestingUserSignUpNotification(User user);

    void sendRestorePasswordNotification(String userEmail, String password);

    void sendUpdatedPasswordNotification(String userEmail);

}