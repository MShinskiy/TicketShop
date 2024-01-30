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
        registry.addViewController("/demo9").setViewName("demo-view9");
        registry.addViewController("/demo6").setViewName("demo-view6");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/administration/index").setViewName("index");
    }
}
