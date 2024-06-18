package com.cursoSecurity.app_security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.List;

/**
 * Configuración estandar, no es necesario ponerla
 */
@Configuration
public class SecurityConfig {
  @Bean
    //sino uso esta anotación nunca se va a añadir al contenedor de Spring
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

    http.authorizeHttpRequests(auth ->
                   // auth.requestMatchers("/loans", "/balance", "/account", "/cards")
                    //Cambiamos hasAuthority por hasRole para trabajar con ROles en vez de autorización.
                    auth.requestMatchers("/loans").hasRole("USER")
                            .requestMatchers("/balance").hasRole("USER")
                            .requestMatchers("/cards").hasRole("ADMIN")
                            //indica el hasAnyAuthority que se pueda usar mas de un rol
                            .requestMatchers("/accounts").hasAnyRole("ADMIN")
                            .anyRequest().permitAll()) //cualquier request mandada tiene que tener autenticación
            .formLogin(Customizer.withDefaults()) // formato del login
            .httpBasic(Customizer.withDefaults()); // tipo de autonticación http, usuario y contraseña
    http.cors(cors -> corsConfigurationSource());
    http.csrf(csrf-> csrf.csrfTokenRequestHandler(requestHandler)
            .ignoringRequestMatchers("/welcome","/aboutUs")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
    return http.build();
  }

  /**
   * se comenta por que se va usar los datos de la base de datos
   *
   * @Bean InMemoryUserDetailsManager inMemoryUserDetailsManager() {
   * UserDetails admin = User.withUsername("admin")
   * .password("to_be_enconded")
   * .authorities("ADMIN")
   * .build();
   * <p>
   * UserDetails user = User.withUsername("user")
   * .password("to_be_enconded")
   * .authorities("USER")
   * .build();
   * <p>
   * return new InMemoryUserDetailsManager(admin, user);
   * }
   * @Bean UserDetailsService userDetailsService(DataSource dataSource){
   * return new JdbcUserDetailsManager(dataSource);
   * }
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    //return  NoOpPasswordEncoder.getInstance();
    return NoOpPasswordEncoder.getInstance();
  }

  /**
   * Método de configuración de cors en el que solo se permite los metodos get y post
   *
   * @return
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    //Url especificas permitidas
    // config.setAllowedOrigins(List.of("http://localhost:8080/"));
    config.setAllowedOrigins(List.of("*"));
    config.setAllowedOrigins(List.of("GET", "POST"));
    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //proteger
    source.registerCorsConfiguration("/**", config);
    return source;

  }

}
