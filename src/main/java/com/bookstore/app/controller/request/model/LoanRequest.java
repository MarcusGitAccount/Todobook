package com.bookstore.app.controller.request.model;

import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoanRequest {

  private User user;

  private Book book;

}
