package bo.dynamicworkflow.dynamicworkflowbackend.app.access.annotations;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ResourceAction {

    @Nonbinding
    ActionCode actionCode();

}