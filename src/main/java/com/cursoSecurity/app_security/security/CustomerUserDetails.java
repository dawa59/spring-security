package com.cursoSecurity.app_security.security;

import com.cursoSecurity.app_security.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CustomerUserDetails implements UserDetailsService {

  private final CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.customerRepository.findByEmail(username)
            .map(customer ->{
              var authorities = List.of( new SimpleGrantedAuthority(customer.getRol()));
              return new User(customer.getEmail(), customer.getPwd(),authorities);
                    }).orElseThrow(() ->new UsernameNotFoundException("User No content"));

  }
}
