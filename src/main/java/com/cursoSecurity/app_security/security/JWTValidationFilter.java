package com.cursoSecurity.app_security.security;

import com.cursoSecurity.app_security.services.JWTService;
import com.cursoSecurity.app_security.services.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final JwtUserDetailsService jwtUserDetailsService;

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

    String userName = null;
    String jwt = null;

    if(Objects.nonNull(requestTokenHeader) && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER)){
      jwt = requestTokenHeader.substring(7);
      try {
        userName = jwtService.getUserNameFromToken(jwt);
      }catch (IllegalArgumentException e){
        log.error(e.getMessage());
      }catch (ExpiredJwtException e){
        log.warn(e.getMessage());
      }
    }

    if(Objects.nonNull(userName) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())){
      final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);

      if( jwtService.validateToken(jwt, userDetails)){
        UsernamePasswordAuthenticationToken userNameAndPassToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        userNameAndPassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(userNameAndPassToken);

      }
    }
    filterChain.doFilter(request, response);
  }
}
