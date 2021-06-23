package bo.dynamicworkflow.dynamicworkflowbackend.app.access;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations.ResourceAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.action.ActionNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Action;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.UserAction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.ActionRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.UserActionRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.UserAccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AccessHandlerInterceptor implements HandlerInterceptor {

    private final UserActionRepository userActionRepository;
    private final ActionRepository actionRepository;

    public AccessHandlerInterceptor(UserActionRepository userActionRepository, ActionRepository actionRepository) {
        this.userActionRepository = userActionRepository;
        this.actionRepository = actionRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("Pre Handle method is Calling");

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResourceAction resourceAction = handlerMethod.getMethod().getAnnotation(ResourceAction.class);
            if (resourceAction != null) {
                ActionCode actionCode = resourceAction.actionCode();
                if (actionRequiresAuth(actionCode)) {
                    UserAccountDto userAccountDto = SessionHolder.getCurrentUserAccount();
                    if (userAccountDto == null || !hasAccessToTheResource(actionCode, userAccountDto.getId())) {
                        response.getWriter().write("User not authorized to access this resource.");
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        return false;
                    }
                }
                setActionIdInSessionHolder(actionCode);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("Post Handle method is Calling");

        registerActionExecutedByUser();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("Request and Response is completed");

        SessionHolder.clear();
    }

    private Boolean actionRequiresAuth(ActionCode actionCode) {
        List<ActionCode> actionsCodeWithoutAuthentication = ActionCode.getActionsCodeWithoutAuth();
        return !actionsCodeWithoutAuthentication.contains(actionCode);
    }

    private Boolean hasAccessToTheResource(ActionCode actionCode, Integer userId) {
        List<UserAction> userActions = userActionRepository.getAllByUserId(userId);
        return userActions.stream().anyMatch(userAction -> userAction.getAction().getCode().equals(actionCode));
    }

    private void setActionIdInSessionHolder(ActionCode actionCode) throws ActionNotFoundException {
        Action action = actionRepository.findByCode(actionCode)
                .orElseThrow(() -> new ActionNotFoundException(
                        String.format("No se pudo encontrar la Acción con code: %s", actionCode.name())
                ));
        SessionHolder.setCurrentActionId(action.getId());
    }

    private void registerActionExecutedByUser() {
        /*
        Integer userId = SessionHolder.getCurrentUserId();
        Integer actionId = SessionHolder.getCurrentActionId();
        if (userId != null && actionId != null) {
            Event event = new Event();
            event.setExecutionDate(TimestampUtility.getCurrentTimestamp());
            event.setUserId(userId);
            event.setActionId(actionId);
            eventRepository.saveAndFlush(event);
        }
         */
    }

}