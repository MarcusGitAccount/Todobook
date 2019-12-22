package com.bookstore.app.controller.response.model;

import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Comment;
import com.bookstore.app.entity.Company;
import com.bookstore.app.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseEntity extends ResponseModel<User> {
  private UUID uid;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private Company company;

  private Boolean isAdmin;

  private List<Comment> comments;

  private List<Book> loans;

  private List<Book> wishlistBooks;

  public UserResponseEntity(User user) {
    super(user);
  }
}
