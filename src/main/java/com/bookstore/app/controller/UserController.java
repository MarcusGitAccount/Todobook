package com.bookstore.app.controller;

import com.bookstore.app.controller.response.model.UserResponseEntity;
import com.bookstore.app.entity.Company;
import com.bookstore.app.entity.User;
import com.bookstore.app.middleware.util.MiddlewareUtils;
import com.bookstore.app.repo.CompanyRepo;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import com.bookstore.app.validation.UserValidation;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.bookstore.app.controller.util.Constants.*;

@RestController
public class UserController {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private CompanyRepo companyRepo;

  @Autowired
  private UserValidation userValidation;

  @GetMapping({"/api/user", "/admin/user"})
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity get(@RequestHeader("authorization") String token) {
    String email = MiddlewareUtils.getEmailFromToken(token);
    User user = userRepo.findByEmail(email).orElse(null);

    if (user == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "No user found.");
    }

    return APIResponseFactory
        .buildDefaultSuccesMessage(new UserResponseEntity(user), "Retrieved logged in user.");
  }

  @GetMapping("/admin/user/{id}")
  public ResponseEntity get(@PathVariable UUID id) {
    User existing = userRepo.findById(id).orElse(null);

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    existing = userRepo.save(existing.toBuilder().build());
    if (existing == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory
        .buildSuccesMessage(new UserResponseEntity(existing), ENTITY_MODIFIED, HttpStatus.OK);
  }

   // Users are to be created only via sign up
  @PostMapping("/admin/user")
  public ResponseEntity post(@RequestBody User user) {
    return APIResponseFactory.buildSuccesMessage(null, "Not implemented", HttpStatus.NOT_IMPLEMENTED);
  }

  @PutMapping({"/admin/user/{id}", "/api/user/{id}"})
  public ResponseEntity put(@RequestBody User user,
                            @PathVariable UUID id,
                            @RequestHeader("authorization") String token) {

    UUID companyId = null;
    if (user.getCompany() != null) {
      companyId = user.getCompany().getUid();
    }

    User existing = userRepo.findById(id).orElse(null);
    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    String email = MiddlewareUtils.getEmailFromToken(token);

    // TODO added security level, do this in a filter.
    if (!userRepo.findByEmail(email).map(User::getIsAdmin).orElse(false)) {
      // if not admin, can modify only logged in user
      if (!user.getEmail().equals(email)) {
        return APIResponseFactory.buildErrorMessage(null, "Cannot modify other users", HttpStatus.UNAUTHORIZED);
      }
    }

    if (companyId != null) {
      Company company = companyRepo.findById(companyId).orElse(null);
      if (company != null) {
        user.setCompany(company);
      }
    } else {
      user.setCompany(null);
    }

    // we don't want to change these, yet
    user.setIsAdmin(existing.getIsAdmin());
    user.setUid(existing.getUid());
    user.setPassword(existing.getPassword());
    user.setEmail(existing.getEmail());
    user.setSaltedHash(existing.getSaltedHash());

    ValidationMessage validationMessage = userValidation.validate(user);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }
    user = userRepo.save(user);
    if (user == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory
        .buildSuccesMessage(new UserResponseEntity(user), ENTITY_MODIFIED, HttpStatus.OK);
  }

  @DeleteMapping("/admin/user/{id}")
  public ResponseEntity deleteById(@PathVariable UUID id) {
    Boolean wasRemoved = false;
    User existing = userRepo.findById(id).orElse(null);

    if (id == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_NOT_FOUND);
    }
    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_DELETE_FAILED, HttpStatus.NOT_FOUND);
    }
    userRepo.delete(existing);
    return APIResponseFactory
        .buildDefaultSuccesMessage(null, ENTITY_DELETED);
  }
}
