package bo.dynamicworkflow.dynamicworkflowbackend.app.configs;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.AccessHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAccessConfig implements WebMvcConfigurer {

    private final AccessHandlerInterceptor accessHandlerInterceptor;

    public WebAccessConfig(AccessHandlerInterceptor accessHandlerInterceptor) {
        this.accessHandlerInterceptor = accessHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessHandlerInterceptor);
    }

}