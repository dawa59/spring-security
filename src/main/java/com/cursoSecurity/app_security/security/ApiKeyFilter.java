/**
 * Se comenta todo por que no se va a usar por que se implementa el JWT
 * package com.cursoSecurity.app_security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class ApiKeyFilter extends OncePerRequestFilter {

  private static final String API_KEY = "myKey";
  private static final String API_KEY_HEADER = "api_Key";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    try {

      final Optional<String> apiKeyOptional = Optional.of(request.getHeader(API_KEY_HEADER));
      final String apiKey = apiKeyOptional.orElseThrow(() -> new BadCredentialsException("no header apiKey"));

      if (!apiKey.equals(API_KEY)) {
        throw new BadCredentialsException("Invalid apikey");
      }

    } catch (Exception e) {
      throw new BadCredentialsException("Invalid Key Pepe");
    }

  filterChain.doFilter(request, response);
  }
}
*/