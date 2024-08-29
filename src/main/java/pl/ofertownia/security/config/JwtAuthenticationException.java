package pl.ofertownia.security.config;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException  extends AuthenticationException {
    public JwtAuthenticationException(String explanation) {
        super(explanation);
    }
}
