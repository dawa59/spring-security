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
public class CustomerUserDetails{ //implements UserDetailsService {

  private final CustomerRepository customerRepository;
/**
 * Se comenta por usar el autenticathion provide
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.customerRepository.findByEmail(username)
            .map(customer ->{
              var authorities = List.of( new SimpleGrantedAuthority(customer.getRol()));
              return new User(customer.getEmail(), customer.getPwd(),authorities);
                    }).orElseThrow(() ->new UsernameNotFoundException("User No content"));

  }
  */
  /**
   * Es lo mismo que lo de arriba, pero
   * public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   *     Optional<Customer> optionalCustomer = this.customerRepository.findByEmail(username);
   *
   *     if (optionalCustomer.isPresent()) {
   *         Customer customer = optionalCustomer.get();
   *         List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRol()));
   *         return new User(customer.getEmail(), customer.getPwd(), authorities);
   *     } else {
   *         throw new UsernameNotFoundException("User No content");
   *     }
   * }
   */

}
