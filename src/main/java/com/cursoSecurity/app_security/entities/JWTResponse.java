package com.cursoSecurity.app_security.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTResponse {
  private String jwt;
}
