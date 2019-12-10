package com.bookstore.app.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uid")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  // without this constraint will right pad with zeroes making extraction by id unsuitable
  // use maybe binary instead
  @Column(name = "uid", columnDefinition = "CHAR(36)")
  private UUID uid;

  @Column(unique = true)
  private String name;

  private String email;

  private String phoneNumber;
}
