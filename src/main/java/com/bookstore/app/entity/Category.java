package com.bookstore.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "uid", columnDefinition = "CHAR(36)")
  private UUID uid;

  private String name;

  @Column(length = 300)
  private String description;

  @ManyToMany(mappedBy = "categories")
  @JsonBackReference(value = "book-category")
  private List<Book> books;

}
