package com.bookstore.app.entity;

import lombok.*;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import javax.persistence.*;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "uid", columnDefinition = "CHAR(36)")
  private UUID uid;

  private String firstName;

  private String lastName;

  @Column(unique = true)
  private String email;

  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  private Boolean isAdmin;

  private String password;

  private String saltedHash;
}
