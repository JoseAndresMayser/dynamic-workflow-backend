package bo.dynamicworkflow.dynamicworkflowbackend.app.access.filter;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.TokenManager;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.exceptions.InvalidTokenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.configs.SecurityConfig;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.UserService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.UserAccountDto;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AccessRequestFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final UserService userService;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_KEY = "authorization";

    public AccessRequestFilter(TokenManager tokenManager, UserService userService) {
        this.tokenManager = tokenManager;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String urlEndpoint = request.getRequestURI().replaceFirst("/dynamic-workflow", "");
        if (urlEndpoint.startsWith("/rest")) {
            List<String> unprotectedEndpoints = Arrays.asList(SecurityConfig.UNPROTECTED_ENDPOINTS);
            if (!unprotectedEndpoints.contains(urlEndpoint)) {
                String token = request.getHeader("Authorization");
                UserAccountDto account = getAccountFromToken(token);
                if (account == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                    response.getWriter().write("Unauthorized user.");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                SessionHolder.setCurrentUserId(account.getId());
                SessionHolder.setCurrentUsername(account.getUsername());
                SessionHolder.setCurrentUserAccount(account);

                UserDetails userDetails = this.userService.loadUserByUsername(account.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private UserAccountDto getAccountFromToken(String token) {
        String jwtToken = getJwtToken(token);
        if (jwtToken != null)
            try {
                return tokenManager.getAccountFromToken(jwtToken, UserAccountDto.class);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        return null;
    }

    private String getJwtToken(String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            String jwtToken = token.replaceFirst(BEARER_PREFIX, "");
            try {
                if (tokenManager.verifyToken(jwtToken)) return jwtToken;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}