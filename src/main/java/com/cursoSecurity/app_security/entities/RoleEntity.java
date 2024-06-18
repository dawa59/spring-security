package com.cursoSecurity.app_security.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Table(name = "roles")
@Data

public class RoleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger id_customer;

  @Column(name = "role_name")
  private String role_name;

  @Column(name = "description")
  private String description;

}
