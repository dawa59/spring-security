package com.cursoSecurity.app_security.services;

import com.cursoSecurity.app_security.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return customerRepository.findByEmail(username)
            .map(customer -> {
              List<SimpleGrantedAuthority> authorities = customer.getRoles()
                      .stream()
                      .map(role-> new SimpleGrantedAuthority(role.getRole_name()))
                      .toList();
              return new User(customer.getEmail(), customer.getPwd(), authorities);
            }).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
  }
}
