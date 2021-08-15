package bo.dynamicworkflow.dynamicworkflowbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Provider;
import java.security.Security;

@SpringBootApplication
public class DynamicWorkflowBackendApplication {

    public static void main(String[] args) {
//        String providerName = "BC";
//        Provider provider = Security.getProvider(providerName);
//        if (provider == null) {
//            System.out.println(providerName + " provider not installed");
//            return;
//        }
//
//        System.out.println("Provider Name :"+ provider.getName());
//        System.out.println("Provider Version :"+ provider.getVersion());
//        System.out.println("Provider Info:" + provider.getInfo());
        SpringApplication.run(DynamicWorkflowBackendApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

}