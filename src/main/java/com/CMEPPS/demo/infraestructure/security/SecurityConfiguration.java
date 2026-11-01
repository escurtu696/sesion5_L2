package com.CMEPPS.demo.infraestructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/h2-console/**", "/login").permitAll() 
                .antMatchers("/", "/list-todos", "/add-todo", "/delete-todo", "/update-todo").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/list-todos", true) // <-- Fuerza la redirección a /list-todos tras el login
                .permitAll()
            );

        return http.build();
    }

    @Bean
    protected InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin")
            .roles("USER", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(admin);
    }
}