package com.cursoSecurity.app_security.repository;

import com.cursoSecurity.app_security.entities.PartnersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface PartnersRepository extends CrudRepository<PartnersEntity, BigInteger> {

  Optional<PartnersEntity> findByClientId(String clientId);
}
