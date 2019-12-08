package com.bookstore.app.controller;

import com.bookstore.app.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class BaseController {

  @GetMapping()
  public User get() {
    return User.builder()
            .firstName("user")
            .build();
  }
}
