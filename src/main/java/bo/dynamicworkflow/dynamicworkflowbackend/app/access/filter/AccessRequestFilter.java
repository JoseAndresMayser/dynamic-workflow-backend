package bo.dynamicworkflow.dynamicworkflowbackend.app.access.filter;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.SessionHolder;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.TokenManager;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.exceptions.InvalidTokenException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.UserService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.UserAccountDto;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessRequestFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final UserService userService;

    public AccessRequestFilter(TokenManager tokenManager, UserService userService) {
        this.tokenManager = tokenManager;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpStatus.OK.value());
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT,DELETE");
            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, " +
                    "Origin,Accept, " +
                    "X-Requested-With, " +
                    "Content-Type, " +
                    "Access-Control-Request-Method, " +
                    "Access-Control-Request-Headers," +
                    "access-control-allow-origin," +
                    "authorization");
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                String jwtToken = token.substring(7);
                if (tokenManager.verifyToken(jwtToken)) {
                    UserAccountDto account = tokenManager.getAccountFromToken(jwtToken, UserAccountDto.class);
                    if (account != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        SessionHolder.setCurrentUserId(account.getId());
                        SessionHolder.setCurrentUsername(account.getUsername());
                        SessionHolder.setCurrentUserAccount(account);

                        UserDetails userDetails = userService.loadUserByUsername(account.getUsername());
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (Exception e) {
                throw new InvalidTokenException("Error al leer el Token: ".concat(e.getMessage()), e);
            }
        }
        filterChain.doFilter(request, response);
    }

}