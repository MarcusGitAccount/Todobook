package com.bookstore.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
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

  @ManyToMany
  @JoinTable(
      name = "user_book_wishlist",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "book_id"))
//  @JsonManagedReference(value = "wishlist")
  private List<Book> wishlistBooks;

  @OneToMany(mappedBy = "user")
  private List<Comment> comments;

  // TODO: add loan books list
}
