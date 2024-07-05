package com.cursoSecurity.app_security.services;

import com.cursoSecurity.app_security.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CustomerUserDetails implements UserDetailsService {

  private final CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.customerRepository.findByEmail(username)
            .map(customer -> {
              final var roles = customer.getRoles();
              final var authorities = roles
                      .stream()
                      .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                      .collect(Collectors.toList());
              return new User(customer.getEmail(), customer.getPwd(), authorities);
            }).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
