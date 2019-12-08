package com.bookstore.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuthenticationJWTToken {
  @Id
  private String token;

  @Column(unique = true)
  private String userEmail;
}
