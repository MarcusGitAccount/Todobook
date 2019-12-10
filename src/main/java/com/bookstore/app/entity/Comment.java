package com.bookstore.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;
import java.util.Date;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "uid", columnDefinition = "CHAR(36)")
  private UUID uid;

  @ManyToOne
  @JoinColumn(name = "user_id")
//  @JsonBackReference(value = "user-comment")
  private User user;

  @ManyToOne
  @JoinColumn(name = "book_id")
//  @JsonBackReference(value = "book-comment")
  private Book referencedBook;

  // defaulting to current time when it was inserted into the database
  @Basic(optional = false)
  @Column(name = "commentTime", insertable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date commentTime;

  @Column(length = 300)
  private String commentText;
}
