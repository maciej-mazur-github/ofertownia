package pl.ofertownia.security.config;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.text.ParseException;

public class BearerTokenFilter extends HttpFilter {
    private final Logger logger = LoggerFactory.getLogger(BearerTokenFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
    private final JwtService jwtService;

    public BearerTokenFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            logger.debug("Missing Authorization header or empty Bearer token");
            chain.doFilter(request, response);
        } else {
            String compactJwt = authorizationHeader.substring(BEARER_PREFIX.length());
            SignedJWT signedJWT;
            try {
                signedJWT = SignedJWT.parse(compactJwt);
                verifyJwt(signedJWT);
                setSecurityContext(signedJWT);
                chain.doFilter(request, response);
            } catch (ParseException e) {
                JwtAuthenticationException authenticationException = new JwtAuthenticationException("Bearer token could not be parsed");
                logger.debug(e.getMessage());
                failureHandler.onAuthenticationFailure(request, response, authenticationException);
            } catch (JwtAuthenticationException e) {
                logger.debug(e.getMessage());
                failureHandler.onAuthenticationFailure(request, response, e);
            }
        }
    }

    private void verifyJwt(SignedJWT signedJWT) {
        jwtService.verifySignature(signedJWT);
        jwtService.verifyExpirationTime(signedJWT);
    }

    private void setSecurityContext(SignedJWT signedJWT) {
        Authentication authentication = jwtService.createAuthentication(signedJWT);
        SecurityContext securityContext = securityContextHolderStrategy.getContext();
        securityContext.setAuthentication(authentication);
    }
}
