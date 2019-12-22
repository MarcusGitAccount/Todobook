package com.bookstore.app.controller;

import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Comment;
import com.bookstore.app.entity.User;
import com.bookstore.app.middleware.util.MiddlewareUtils;
import com.bookstore.app.repo.BookRepo;
import com.bookstore.app.repo.CommentRepo;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import com.bookstore.app.validation.CommentValidation;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import static com.bookstore.app.controller.util.Constants.*;

@RestController
public class CommentController {

  @Autowired
  private CommentRepo commentRepo;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private BookRepo bookRepo;

  @Autowired
  private CommentValidation commentValidation;

  @GetMapping({"/api/comment", "/admin/comment"})
  public ResponseEntity getAll() {
    List<Comment> result = commentRepo.findAll();

    if (result.isEmpty()) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(result, ENTITY_FOUND);
  }

  @GetMapping({"/api/comment/{id}", "/admin/comment/{id}"})
  public ResponseEntity getById(@PathVariable UUID id) {
    Comment comment = commentRepo.findById(id).orElse(null);

    if (comment == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(comment, ENTITY_FOUND);
  }

  @PostMapping({"/api/comment", "/admin/comment"})
  public ResponseEntity post(@RequestBody Comment comment,
                             @RequestHeader("authorization") String token) {
    ValidationMessage validationMessage = commentValidation.validate(comment);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    ResponseEntity notFound = APIResponseFactory.buildErrorMessage(null,
        ENTITY_CREATION_FAILED, HttpStatus.NOT_FOUND);

    if (comment.getUser() == null || comment.getUser().getUid() == null) {
      return notFound;
    }
    User user = userRepo.findById(comment.getUser().getUid()).orElse(null);
    if (user == null) {
      return notFound;
    }

    if (comment.getReferencedBook() == null || comment.getReferencedBook().getUid() == null) {
      return notFound;
    }
    Book book = bookRepo.findById(comment.getReferencedBook().getUid()).orElse(null);
    if (book == null) {
      return notFound;
    }

    comment.setUser(user);
    comment.setReferencedBook(book);
    if (!userCanProceed(token, comment)) {
      return APIResponseFactory.buildErrorMessage(null, UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    Comment result = commentRepo.save(comment);
    if (result == null) {
      return notFound;
    }

    return APIResponseFactory.buildSuccesMessage(result, ENTITY_CREATED, HttpStatus.CREATED);
  }

  @PutMapping({"/api/comment/{id}", "/admin/comment/{id}"})
  public ResponseEntity put(@RequestBody Comment comment,
                            @PathVariable UUID id,
                            @RequestHeader("authorization") String token) {
    Comment existing = commentRepo.findById(id).orElse(null);

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    if (!userCanProceed(token, comment)) {
      return APIResponseFactory.buildErrorMessage(null, UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    comment.setUid(existing.getUid());
    ValidationMessage validationMessage = commentValidation.validate(comment);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    existing = commentRepo.save(comment);
    if (existing == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }
    return APIResponseFactory.buildSuccesMessage(existing, ENTITY_MODIFIED, HttpStatus.OK);
  }

  @DeleteMapping({"/admin/comment/{id}", "/api/comment/{id}"})
  public ResponseEntity deleteById(@PathVariable UUID id,
                                   @RequestHeader("authorization") String token) {
    Boolean wasRemoved = false;
    Comment existing = commentRepo.findById(id).orElse(null);

    if (id == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_NOT_FOUND);
    }
    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_DELETE_FAILED, HttpStatus.NOT_FOUND);
    }
    if (!userCanProceed(token, existing)) {
      return APIResponseFactory.buildErrorMessage(null, UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    commentRepo.delete(existing);
    return APIResponseFactory.buildDefaultSuccesMessage(existing, ENTITY_DELETED);
  }

  private boolean userCanProceed(String token, Comment comment) {
    String email = MiddlewareUtils.getEmailFromToken(token);
    User user = userRepo.findByEmail(email).orElse(null);

    if (user == null || comment.getUser() == null) {
      return false;
    }
    if (!user.getIsAdmin() && !user.getEmail().equals(comment.getUser().getEmail())) {
      return false;
    }
    return true;
  }
}
