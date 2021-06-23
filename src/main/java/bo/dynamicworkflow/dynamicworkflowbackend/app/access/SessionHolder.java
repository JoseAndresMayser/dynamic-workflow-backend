package bo.dynamicworkflow.dynamicworkflowbackend.app.access;

import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.UserAccountDto;

public class SessionHolder {

    private static final ThreadLocal<Integer> currentUserId = new InheritableThreadLocal<>();
    private static final ThreadLocal<String> currentUsername = new InheritableThreadLocal<>();
    private static final ThreadLocal<Integer> currentActionId = new InheritableThreadLocal<>();
    private static final ThreadLocal<UserAccountDto> currentUserAccount = new InheritableThreadLocal<>();

    public static Integer getCurrentUserId() {
        return currentUserId.get();
    }

    public static String getCurrentUsername() {
        return currentUsername.get();
    }

    public static Integer getCurrentActionId() {
        return currentActionId.get();
    }

    public static UserAccountDto getCurrentUserAccount() {
        return currentUserAccount.get();
    }

    public static void setCurrentUserId(Integer userId) {
        currentUserId.set(userId);
    }

    public static void setCurrentUsername(String username) {
        currentUsername.set(username);
    }

    public static void setCurrentActionId(Integer actionId) {
        currentActionId.set(actionId);
    }

    public static void setCurrentUserAccount(UserAccountDto userAccount) {
        currentUserAccount.set(userAccount);
    }

    public static void clear() {
        currentUserId.remove();
        currentUserId.remove();
        currentActionId.remove();
        currentUserAccount.remove();
    }

}