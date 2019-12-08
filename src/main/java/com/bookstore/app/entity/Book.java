package com.bookstore.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.UUID;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "uid", columnDefinition = "CHAR(36)")
  private UUID uid;

  private String name;

  @Column(length = 300)
  private String description;

  private Boolean isAvailableToLoan;

  // One book is written by only one author.
  // TODO: Make this relation many to many.
  @ManyToOne
  @JoinColumn(name = "author_id")
  private Author author;

  @ManyToMany
  @JoinTable(
      name = "book_categories",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  @JsonManagedReference // to prevent infinite loop queries
  private LinkedList<Category> categories;

  @ManyToMany(mappedBy = "wishlistBooks")
  @JsonBackReference
  private LinkedList<User> usersWishlists;

  @OneToMany
  @JoinColumn(name = "comment_id")
  @JsonManagedReference
  private LinkedList<Comment> comments;
}
