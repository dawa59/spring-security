package com.cursoSecurity.app_security.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.management.relation.Role;
import java.math.BigInteger;
import java.util.List;

@Entity

@Table(name = "customers")
@Data
public class CustomerEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger id;

  @Column(name = "email")
  private String email;

  @Column(name = "pwd")
  private String pwd;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_customer")
  private List<RoleEntity> roles;

}
