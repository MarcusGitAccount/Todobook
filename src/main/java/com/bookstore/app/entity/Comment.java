package com.bookstore.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.UUID;
import java.util.Date;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uid")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "uid", columnDefinition = "CHAR(36)")
  private UUID uid;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book referencedBook;

  // defaulting to current time when it was inserted into the database
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date commentTime;

  @Column(length = 300)
  private String commentText;
}
