package pl.ofertownia.security.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import pl.ofertownia.security.user.RoleEnum;

import java.io.IOException;

@Configuration
public class SecurityConfig {
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Configuration
    @Order(1)
    public static class WebsiteConfigurationAdapter {
        @Bean
        public SecurityFilterChain websiteFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
            PathRequest.H2ConsoleRequestMatcher h2ConsoleRequestMatcher = PathRequest.toH2Console();
            http.authorizeHttpRequests(requests -> requests
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers(
                            mvc.pattern("/"),
                            mvc.pattern(HttpMethod.GET, "/api/offers"),
                            mvc.pattern(HttpMethod.GET, "/api/offers/count")).permitAll()
                    .requestMatchers(
                            mvc.pattern("/dodaj-oferte"),
                            mvc.pattern(HttpMethod.POST, "/api/offers/*"),
                            mvc.pattern(HttpMethod.DELETE, "/api/offers/*"))
                    .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPERADMIN.name())
                    .requestMatchers(
                            mvc.pattern(HttpMethod.POST, "/api/categories/*"),
                            mvc.pattern(HttpMethod.DELETE, "/api/categories/*"))
                    .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPERADMIN.name())
                    .requestMatchers(mvc.pattern("/reset-hasla/**")).permitAll()
                    .requestMatchers(mvc.pattern("/rejestracja/**")).permitAll()
                    .requestMatchers(mvc.pattern("/rejestracja")).permitAll()
                    .requestMatchers(mvc.pattern("/odmowa-dostepu")).permitAll()
                    .requestMatchers(mvc.pattern("/api/admin/credentials/*")).hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPERADMIN.name())
                    .requestMatchers(mvc.pattern("/api/admin/delete/*")).hasRole(RoleEnum.SUPERADMIN.name())
                    .requestMatchers(h2ConsoleRequestMatcher).permitAll()
                    .requestMatchers(
                            mvc.pattern("/script/main.js"),
                            mvc.pattern("/script/registration.js"),
                            mvc.pattern("/styles/**")).permitAll()
                    .requestMatchers(mvc.pattern("/img/**")).permitAll()
                    .requestMatchers(mvc.pattern("/api/categories/**")).authenticated()
                    .requestMatchers(mvc.pattern("/kategorie/**")).authenticated()
                    .requestMatchers(mvc.pattern("/edycja/**")).authenticated()
                    .requestMatchers(mvc.pattern("/szukaj/**")).authenticated()
                    .requestMatchers(mvc.pattern("/oferta/**")).authenticated()
                    .requestMatchers(mvc.pattern("/admin/**")).hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPERADMIN.name())
                    .requestMatchers(mvc.pattern("/script/admin.js")).hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPERADMIN.name())

                    .anyRequest().authenticated());
            http.formLogin(login -> login.loginPage("/zaloguj").permitAll());
            http.logout(logout ->
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/wyloguj/**", HttpMethod.GET.name()))
                            .logoutSuccessUrl("/"));
            http.csrf(csrf -> csrf
                    .ignoringRequestMatchers(h2ConsoleRequestMatcher)
            );
            http
                    // ...
                    .csrf((csrf) -> csrf
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                    )
                    .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
            http.headers(config ->
                    config.frameOptions(options ->
                            options.sameOrigin()));
            http.exceptionHandling((exception)-> exception.accessDeniedHandler(new CustomAccessDeniedHandler()));
            DefaultSecurityFilterChain build = http.build();
            return build;
        }
    }

    @Configuration
    @Order(2)
    public static class ApiConfigurationAdapter {
        private final JwtService jwtService;

        public ApiConfigurationAdapter(JwtService jwtService) {
            this.jwtService = jwtService;
        }

        @Bean
        public SecurityFilterChain apiFilterChain(HttpSecurity http,
                                               MvcRequestMatcher.Builder apiMvc,
                                               AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtService);
            BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(jwtService);
            http.securityMatcher("/api/v2/**")
                    .authorizeHttpRequests(requests -> requests
                    .requestMatchers(
                            apiMvc.pattern(HttpMethod.GET, "/api/v2/offers/count"),
                            apiMvc.pattern(HttpMethod.GET, "/api/v2/offers"))
                                .authenticated()
                    .requestMatchers(
                            apiMvc.pattern(HttpMethod.POST, "/api/v2/offers"),
                            apiMvc.pattern(HttpMethod.DELETE, "/api/v2/offers/*"))
                                .hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPERADMIN.name())
                    .requestMatchers(apiMvc.pattern(HttpMethod.GET, "/api/v2/offers/*")).authenticated()

            );

            http.exceptionHandling(
                    ex -> ex.authenticationEntryPoint(
                            new RestAuthenticationEntryPoint()
                    )
            );
            http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.csrf(csrfCustomizer -> csrfCustomizer.disable());
            http.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
            http.addFilterBefore(bearerTokenFilter, AuthorizationFilter.class);
            DefaultSecurityFilterChain build = http.build();
            return build;
        }

        private class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");
            }
        }
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
