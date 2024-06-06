package com.cursoSecurity.app_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración estandar, no es necesario ponerla
 */
@Configuration
public class SecurityConfig {
    @Bean
        //sino uso esta anotación nunca se va a añadir al contenedor de Spring
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/loans", "/balance", "/account", "/cards")
                                .authenticated()
                                .anyRequest().permitAll()) //cualquier request mandada tiene que tener autenticación
                .formLogin(Customizer.withDefaults()) // formato del login
                .httpBasic(Customizer.withDefaults()); // tipo de autonticación http, usuario y contraseña

        return http.build();
    }

    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails admin = User.withUsername("admin")
                .password("to_be_enconded")
                .authorities("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password("to_be_enconded")
                .authorities("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return  NoOpPasswordEncoder.getInstance();
    }
}
