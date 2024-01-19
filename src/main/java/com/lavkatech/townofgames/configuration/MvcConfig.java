package com.lavkatech.townofgames.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/administration/panel").setViewName("admin-panel");
        registry.addViewController("/administration/edit").setViewName("edit-panel");
        registry.addViewController("/administration/login").setViewName("login");
        //registry.addViewController("/map7").setViewName("field7");
        //registry.addViewController("/map10").setViewName("field10");
        //registry.addViewController("/map").setViewName("index");
    }
}