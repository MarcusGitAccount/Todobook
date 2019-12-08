package com.bookstore.app.controller.response.model;

import com.bookstore.app.entity.Company;
import com.bookstore.app.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

  public UserResponseEntity(User user) {
    super(user);
  }
}
