package com.cursoSecurity.app_security.security;

import com.cursoSecurity.app_security.services.CustomerUserDetails;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

  private static final String LOGIN_RESOURCE = "/login";
  private static final String [] USER_RESOURCES = {"/loans/**", "/balance/**"};
  private static final String [] ADMIN_RESOURCES = {"/accounts/**", "/cards/**"};
  private static final String AUTH_WRITE = "write";
  private static final String AUTH_READ = "read";
  private static final String ROLE_ADMIN = "ADMIN";
  private static final String ROLE_USER = "USER";



  @Bean
  @Order(1)
  SecurityFilterChain auth2SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
    httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());

    httpSecurity.exceptionHandling(e ->
            e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));

    return httpSecurity.build();
  }

  @Bean
  @Order(2)
  /**
   * Confifguración del cliente
   */
  SecurityFilterChain clientSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.formLogin(Customizer.withDefaults());
    httpSecurity.authorizeHttpRequests(auth -> auth
            .requestMatchers(ADMIN_RESOURCES).hasRole(ROLE_ADMIN)
            .requestMatchers(USER_RESOURCES).hasRole(ROLE_USER)
            .anyRequest().permitAll());
    httpSecurity.oauth2ResourceServer(oauth -> oauth
            .jwt(Customizer.withDefaults()));

    return httpSecurity.build();
  }

  @Bean
  @Order(3)
  /**
   * Configuración de los roles de usuario
   */
  SecurityFilterChain userSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {


    httpSecurity.authorizeHttpRequests(auth -> auth
            .requestMatchers(ADMIN_RESOURCES).hasRole(AUTH_WRITE)
            .requestMatchers(USER_RESOURCES).hasAuthority(AUTH_READ)
            .anyRequest().permitAll());


    return httpSecurity.build();
  }


  @Bean
  /**
   *Para hashear el password. Se usa en el método Main
   */
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  /**
   * Método para codificar contraseñas con BCrypt y el servicio que proporciona detalles del usuario
   */
  AuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomerUserDetails userDetails) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(encoder);
    authProvider.setUserDetailsService(userDetails);
    return authProvider;
  }

  @Bean
  AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
  }

  @Bean
  /**
   * Parsear los Granted Authorities para quitar el prefijo ROLE_
   */
  JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");
    return converter;
  }

  @Bean
  /**
   * configuración del Authentication.
   */
  JwtAuthenticationConverter jwtAuthenticationConverter (JwtGrantedAuthoritiesConverter settings){
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(settings);
    return converter;
  }

}
