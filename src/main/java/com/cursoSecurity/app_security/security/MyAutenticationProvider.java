package com.cursoSecurity.app_security.security;

import com.cursoSecurity.app_security.entities.CustomerEntity;
import com.cursoSecurity.app_security.entities.RoleEntity;
import com.cursoSecurity.app_security.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class MyAutenticationProvider implements AuthenticationProvider {

  private CustomerRepository customerRepository;
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String userName = authentication.getName();
    final String pwd = authentication.getCredentials().toString();

    final Optional<CustomerEntity> customerDB = this.customerRepository.findByEmail(userName);
    final CustomerEntity customer = customerDB.orElseThrow(() -> new BadCredentialsException("Invalid Credentials"));
    final String customerPwd = customer.getPwd();

    if(passwordEncoder.matches(pwd, customerPwd)){
      //final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRol()));
      final List<RoleEntity> roles = customer.getRoles();
      final List<SimpleGrantedAuthority> authorities = roles
              .stream()
              .map(role -> new SimpleGrantedAuthority(role.getRole_name()))
              .collect(Collectors.toList());

      return new UsernamePasswordAuthenticationToken(userName,pwd, authorities);
    }else{
      throw new BadCredentialsException("Invalid Credentials");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
