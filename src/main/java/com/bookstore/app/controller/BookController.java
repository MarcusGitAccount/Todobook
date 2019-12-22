package com.bookstore.app.controller;

import com.bookstore.app.entity.Author;
import com.bookstore.app.entity.Book;
import com.bookstore.app.repo.AuthorRepo;
import com.bookstore.app.repo.BookRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import com.bookstore.app.validation.BookValidation;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import static com.bookstore.app.controller.util.Constants.*;

@RestController
public class BookController {

  @Autowired
  private BookRepo bookRepo;

  @Autowired
  private AuthorRepo authorRepo;

  @Autowired
  private BookValidation bookValidation;

  @GetMapping({"/api/book", "/admin/book"})
  public ResponseEntity getAll() {
    List<Book> result = bookRepo.findAll();

    if (result.isEmpty()) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(result, ENTITY_FOUND);
  }

  @GetMapping({"/api/book/{id}", "/admin/book/{id}"})
  public ResponseEntity getById(@PathVariable UUID id) {
    Book book = bookRepo.findById(id).orElse(null);

    if (book == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(book, ENTITY_FOUND);
  }

  @PostMapping("/admin/book")
  public ResponseEntity post(@RequestBody Book book) {
    ValidationMessage validationMessage = bookValidation.validate(book);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    ResponseEntity notFound = APIResponseFactory.buildErrorMessage(null,
        ENTITY_CREATION_FAILED, HttpStatus.NOT_FOUND);
    if (book.getAuthor() == null || book.getAuthor().getUid() == null) {
      return notFound;
    }

    Author author = authorRepo.findById(book.getAuthor().getUid()).orElse(null);
    if (author == null) {
      return notFound;
    }
    if (bookRepo.findByName(book.getName()).orElse(null) != null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book already registered.");
    }

    book.setAuthor(author);

    Book result = bookRepo.save(book);
    if (result == null) {
      return notFound;
    }

    return APIResponseFactory.buildSuccesMessage(result, ENTITY_CREATED, HttpStatus.CREATED);
  }

  @PutMapping("/admin/book/{id}")
  public ResponseEntity put(@RequestBody Book book, @PathVariable UUID id) {
    Book existing = bookRepo.findById(id).orElse(null);

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    book.setUid(existing.getUid());
    ValidationMessage validationMessage = bookValidation.validate(book);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    existing = bookRepo.save(book);
    if (existing == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }
    return APIResponseFactory.buildSuccesMessage(existing, ENTITY_MODIFIED, HttpStatus.OK);
  }

  @DeleteMapping("/admin/book/{id}")
  public ResponseEntity deleteById(@PathVariable UUID id) {
    Boolean wasRemoved = false;
    Book existing = bookRepo.findById(id).orElse(null);

    if (id == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_NOT_FOUND);
    }
    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_DELETE_FAILED, HttpStatus.NOT_FOUND);
    }
    bookRepo.delete(existing);
    return APIResponseFactory.buildDefaultSuccesMessage(null, ENTITY_DELETED);
  }
}
