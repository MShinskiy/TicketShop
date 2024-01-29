package com.lavkatech.townofgames.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .headers((headers) -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                        .contentTypeOptions(HeadersConfigurer.ContentTypeOptionsConfig::disable)
                        .httpStrictTransportSecurity(hsts -> hsts
                            .includeSubDomains(true)
                                .preload(true)
                                .maxAgeInSeconds(31536000)
                        ))
                .csrf((csrf) -> csrf.ignoringRequestMatchers("/*"))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(mvcMatcherBuilder.pattern("/administration/**")).authenticated()
                        .requestMatchers(mvcMatcherBuilder.pattern("/**"), mvcMatcherBuilder.pattern("/img/*"), mvcMatcherBuilder.pattern("/resources/**"), mvcMatcherBuilder.pattern("/static/**")).permitAll()
                )
                .formLogin((form) -> form
                        //.loginPage("/administration/login")
                        .successForwardUrl("/administration/panel")
                        .defaultSuccessUrl("/administration/panel")
                        .permitAll()
                )
                //.logout(LogoutConfigurer::permitAll);
                ;

        return http.getOrBuild();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("lavkatech")
                        .roles("ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
