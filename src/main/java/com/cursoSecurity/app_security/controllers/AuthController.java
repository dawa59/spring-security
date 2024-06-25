package com.cursoSecurity.app_security.controllers;

import com.cursoSecurity.app_security.entities.JWTRequest;
import com.cursoSecurity.app_security.entities.JWTResponse;
import com.cursoSecurity.app_security.services.JWTService;
import com.cursoSecurity.app_security.services.JwtUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUserDetailsService jwtUserDetailsService;
  private JWTService jwtService;

  @PostMapping ("/authenticate")
  public ResponseEntity<?> postToken(@RequestBody JWTRequest request) {
    authenticate(request);
    final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUserName());
    final String token = jwtService.generationToken(userDetails);
    return ResponseEntity.ok(new JWTResponse(token));
  }

  private void authenticate(JWTRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassWord()));
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException(e.getMessage());
    }catch (DisabledException e) {
      throw new DisabledException(e.getMessage());
    }
  }

}
