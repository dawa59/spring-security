package com.cursoSecurity.app_security.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


//@Component comento para que Spring no cargue este componente.
// No se usa por el autenticathion provider
public class MyPasswordEncoder implements PasswordEncoder {

  @Override
  public String encode(CharSequence rawPassword) {
    return String.valueOf(rawPassword.toString().hashCode());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    String paswordAssString = String.valueOf(rawPassword.toString().hashCode());
    return encodedPassword.equals(paswordAssString);
  }
}
