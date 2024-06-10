package com.cursoSecurity.app_security.repository;

import com.cursoSecurity.app_security.entities.CustomerEntities;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<CustomerEntities, BigInteger> {

  Optional<CustomerEntities>findByEmail(String email);
}
