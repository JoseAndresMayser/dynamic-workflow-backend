package bo.dynamicworkflow.dynamicworkflowbackend.app.notification;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;

import java.util.List;

public interface NotificationService {

    void sendRequestingUserSignUpNotification(User user);

    void sendRestorePasswordNotification(String userEmail, String password);

    void sendUpdatedPasswordNotification(String userEmail);

    void sendNotificationToStageAnalysts(List<String> analystAddressees, String requestingFullName, String processName,
                                         String requestCode, String requestFormFullPath);

    void sendRegisteredNewRequestNotification(String requestingEmail, String processName, String requestCode,
                                              String requestFormFullPath);

}