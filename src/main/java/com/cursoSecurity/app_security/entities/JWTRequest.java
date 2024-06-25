package com.cursoSecurity.app_security.entities;

import lombok.Data;

@Data
public class JWTRequest {
  private String userName;
  private String passWord;
}
