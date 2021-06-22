package bo.dynamicworkflow.dynamicworkflowbackend.app.access.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccessAuthenticationEntryPoint accessAuthenticationEntryPoint;
    private final AccessRequestFilter accessRequestFilter;

    public WebSecurityConfig(AccessAuthenticationEntryPoint accessAuthenticationEntryPoint,
                             AccessRequestFilter accessRequestFilter) {
        this.accessAuthenticationEntryPoint = accessAuthenticationEntryPoint;
        this.accessRequestFilter = accessRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers(UNPROTECTED_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(accessAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout().deleteCookies("JSESSIONID").invalidateHttpSession(true);

        httpSecurity.addFilterBefore(accessRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private static final String[] UNPROTECTED_ENDPOINTS = {
            "/access/log-in",
            "/access/password/restore",
            "/users/requesting"
    };

}