package pl.ofertownia.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import pl.ofertownia.security.user.RoleEnum;

@Configuration
public class SecurityConfig {
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        PathRequest.H2ConsoleRequestMatcher h2ConsoleRequestMatcher = PathRequest.toH2Console();
        http.authorizeHttpRequests(requests -> requests
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
                .requestMatchers(mvc.pattern("/api/admin/credentials/*")).hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPERADMIN.name())
                .requestMatchers(mvc.pattern("/api/admin/delete/*")).hasRole(RoleEnum.SUPERADMIN.name())
                .requestMatchers(h2ConsoleRequestMatcher).permitAll()
                .requestMatchers(mvc.pattern("/script/main.js"), mvc.pattern("/styles/**")).permitAll()
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
//        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
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
//        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.exceptionHandling((exception)-> exception.accessDeniedHandler(accessDeniedHandler()));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
}
