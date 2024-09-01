package pl.ofertownia.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/script").setViewName("welcome");
//        registry.addViewController("/").setViewName("welcome");
//        registry.addViewController("/hello").setViewName("hello");
//        registry.addViewController("/admin").setViewName("admin");
//        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/registration").setViewName("registration");
//    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/script/**")
                .addResourceLocations("classpath:/static/script/");
        registry
                .addResourceHandler("/styles/**")
                .addResourceLocations("classpath:/static/styles/");
        registry
                .addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
    }

}