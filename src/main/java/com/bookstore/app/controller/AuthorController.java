package com.bookstore.app.controller;

import com.bookstore.app.entity.Author;
import com.bookstore.app.repo.AuthorRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import com.bookstore.app.validation.AuthorValidation;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import static com.bookstore.app.controller.util.Constants.*;

@RestController
public class AuthorController {

  @Autowired
  private AuthorRepo authorRepo;

  @Autowired
  private AuthorValidation authorValidation;

  @GetMapping({"/api/author", "/admin/author"})
  public ResponseEntity get() {
    List<Author> authors = authorRepo.findAll();

    if (authors.size() == 0) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_NOT_FOUND);
    }

    return APIResponseFactory
        .buildDefaultSuccesMessage(authors, ENTITY_FOUND);
  }

  @GetMapping({"/api/author/{id}", "/admin/author/{id}"})
  public ResponseEntity getById(@PathVariable UUID id) {
    Author existing = authorRepo.findById(id).orElse(null);

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory
        .buildDefaultSuccesMessage(existing, ENTITY_MODIFIED);
  }

  @PostMapping("/admin/author")
  public ResponseEntity post(@RequestBody Author author) {
    ValidationMessage validationMessage = authorValidation.validate(author);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    Author result = authorRepo.save(author);
    if (result == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_CREATION_FAILED, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildSuccesMessage(result, ENTITY_CREATED, HttpStatus.CREATED);
  }

  @PutMapping("/admin/author/{id}")
  public ResponseEntity put(@RequestBody Author author, @PathVariable UUID id) {
    Author existing = authorRepo.findById(id).orElse(null);

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    author.setUid(existing.getUid());
    ValidationMessage validationMessage = authorValidation.validate(author);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    existing = authorRepo.save(author);
    if (existing == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory.buildSuccesMessage(existing, ENTITY_MODIFIED, HttpStatus.OK);
  }

  @DeleteMapping("/admin/author/{id}")
  public ResponseEntity deleteById(@PathVariable UUID id) {
    Boolean wasRemoved = false;
    Author existing = authorRepo.findById(id).orElse(null);

    if (id == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_NOT_FOUND);
    }
    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_DELETE_FAILED, HttpStatus.NOT_FOUND);
    }
    authorRepo.delete(existing);
    return APIResponseFactory
        .buildDefaultSuccesMessage(null, ENTITY_DELETED);
  }
}
