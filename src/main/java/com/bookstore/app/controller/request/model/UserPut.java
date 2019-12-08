package com.bookstore.app.controller.request.model;

import com.bookstore.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Used as the request body for the /api/user PUT request

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPut {
  User user;
  UUID companyId;
}
