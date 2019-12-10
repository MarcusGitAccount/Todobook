package com.bookstore.app.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uid")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "uid", columnDefinition = "CHAR(36)")
  private UUID uid;

  @Column(unique = true)
  private String name;

  @Column(length = 300)
  private String description;

  @Column(nullable = false)
  private Boolean isAvailableToLoan = true;

  // One book is written by only one author.
  // TODO: Make this relation many to many.
  @ManyToOne
  @JoinColumn(name = "author_id")
//  @JsonBackReference(value = "book-author")
  private Author author;

  @ManyToMany
  @JoinTable(
      name = "book_categories",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
//  @JsonManagedReference(value = "book-category") // to prevent infinite loop queries
  private List<Category> categories;

  @ManyToMany(mappedBy = "wishlistBooks")
//  @JsonBackReference(value = "wishlist")
  private List<User> usersWishlists;

  @OneToMany
  @JoinColumn(name = "comment_id")
//  @JsonManagedReference(value = "book-comment")
  private List<Comment> comments;
}
