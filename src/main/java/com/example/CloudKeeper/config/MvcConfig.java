package com.example.CloudKeeper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Value("${app.configuration.cors.origins}")
    private String allowedOrigins;

    @Value("${app.configuration.cors.headers}")
    private String allowedHeaders;

    @Value("${app.configuration.cors.methods}")
    private String allowedMethods;

    @Value("${app.configuration.cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${app.configuration.threadPoolTaskExecutor.corePoolSize}")
    private int corePoolSize;

    @Value("${app.configuration.threadPoolTaskExecutor.maxPoolSize}")
    private int maxPoolSize;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logout").setViewName("logout");
        registry.addViewController("/file").setViewName("file");
        registry.addViewController("/list").setViewName("list");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:8080")
                //.allowedMethods("*")
                //.allowCredentials(true)
                //.allowedHeaders("*")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowCredentials(allowCredentials)
                .allowedHeaders(allowedHeaders);
    }

    @Bean
    public ThreadPoolTaskExecutor mvcTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        return taskExecutor;
    }

    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(mvcTaskExecutor());
    }
}