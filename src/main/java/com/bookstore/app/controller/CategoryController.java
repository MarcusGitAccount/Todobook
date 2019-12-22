package com.bookstore.app.controller;

import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Category;
import com.bookstore.app.repo.BookRepo;
import com.bookstore.app.repo.CategoryRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import com.bookstore.app.validation.CategoryValidation;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.bookstore.app.controller.util.Constants.*;
import static com.bookstore.app.controller.util.Constants.ENTITY_DELETED;
import static com.bookstore.app.controller.util.Constants.ENTITY_DELETE_FAILED;

@RestController
public class CategoryController {
  @Autowired
  private CategoryRepo categoryRepo;

  @Autowired
  private BookRepo bookRepo;

  @Autowired
  private CategoryValidation categoryValidation;

  @GetMapping({"/api/category", "/admin/category"})
  public ResponseEntity getAll() {
    List<Category> result = categoryRepo.findAll();

    if (result.isEmpty()) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(result, ENTITY_FOUND);
  }

  @GetMapping({"/api/category/{id}", "/admin/category/{id}"})
  public ResponseEntity getById(@PathVariable UUID id) {
    Category category = categoryRepo.findById(id).orElse(null);

    if (category == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(category, ENTITY_FOUND);
  }

  @PostMapping("/admin/category")
  public ResponseEntity post(@RequestBody Category category) {
    ValidationMessage validationMessage = categoryValidation.validate(category);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    category.setBooks(new LinkedList<>());
    Category result = categoryRepo.save(category);
    if (result == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }
    return APIResponseFactory.buildSuccesMessage(result, ENTITY_CREATED, HttpStatus.CREATED);
  }

  @PutMapping("/admin/category/{id}")
  public ResponseEntity put(@RequestBody Category category, @PathVariable UUID id) {
    Category existing = categoryRepo.findById(id).orElse(null);

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    category.setUid(existing.getUid());
    ValidationMessage validationMessage = categoryValidation.validate(category);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    existing = categoryRepo.save(category);
    if (existing == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }
    return APIResponseFactory.buildSuccesMessage(existing, ENTITY_MODIFIED, HttpStatus.OK);
  }

  @DeleteMapping("/admin/category/{id}")
  public ResponseEntity deleteById(@PathVariable UUID id) {
    Boolean wasRemoved = false;
    Category existing = categoryRepo.findById(id).orElse(null);

    if (id == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_NOT_FOUND);
    }
    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_DELETE_FAILED, HttpStatus.NOT_FOUND);
    }

    categoryRepo.delete(existing);
    return APIResponseFactory.buildDefaultSuccesMessage(null, ENTITY_DELETED);
  }

  @PostMapping("/admin/category/{id}/book/add")
  public ResponseEntity addBook(@RequestBody Book book, @PathVariable UUID id) {
    Category existingCategory = categoryRepo.findById(id).orElse(null);

    if (existingCategory == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    if (book.getUid() == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book not found");
    }
    Book existingBook = bookRepo.findById(book.getUid()).orElse(null);
    if (existingBook == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book not found");
    }

    if (existingCategory.getBooks().contains(existingBook)) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book already in given category");
    }

    existingBook.getCategories().add(existingCategory);
    existingCategory.getBooks().add(existingBook);

    if (bookRepo.save(existingBook) == null ||
        categoryRepo.save(existingCategory) == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(existingCategory, "Book added to category");
  }

  @PostMapping("/admin/category/{id}/book/remove")
  public ResponseEntity removeBook(@RequestBody Book book, @PathVariable UUID id) {
    Category existingCategory = categoryRepo.findById(id).orElse(null);

    if (existingCategory == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    if (book.getUid() == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book not found");
    }
    Book existingBook = bookRepo.findById(book.getUid()).orElse(null);
    if (existingBook == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book not found");
    }

    if (!existingCategory.getBooks().contains(existingBook)) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book not in given category");
    }

    existingBook.getCategories().remove(existingCategory);
    existingCategory.getBooks().remove(existingBook);

    if (bookRepo.save(existingBook) == null ||
        categoryRepo.save(existingCategory) == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(existingCategory, "Book removed from category");
  }
}
