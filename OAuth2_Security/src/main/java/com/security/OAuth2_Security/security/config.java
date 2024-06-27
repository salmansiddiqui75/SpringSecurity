package com.security.OAuth2_Security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class config
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        security.authorizeRequests(authorizeRequests -> authorizeRequests.requestMatchers("/sec/**").permitAll().anyRequest().
                authenticated()).oauth2Client(Customizer.withDefaults());
        return security.build();
    }
}
