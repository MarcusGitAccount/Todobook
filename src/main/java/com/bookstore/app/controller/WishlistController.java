package com.bookstore.app.controller;

import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.User;
import com.bookstore.app.middleware.util.MiddlewareUtils;
import com.bookstore.app.repo.BookRepo;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bookstore.app.controller.util.Constants.ENTITY_ALTERATION_FAILED;

@RestController
public class WishlistController {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private BookRepo bookRepo;

  @PostMapping({"/admin/wishlist", "/api/wishlist"})
  public ResponseEntity addBook(@RequestBody Book book, @RequestHeader("authorization") String token) {
    String userEmail = MiddlewareUtils.getEmailFromToken(token);
    User user = userRepo.findByEmail(userEmail).orElse(null);

    if (user == null || user.getUid() == null) {
      return APIResponseFactory.buildErrorMessage(null, "User not found", HttpStatus.NOT_FOUND);
    }

    if (book == null || book.getUid() == null) {
      return APIResponseFactory.buildErrorMessage(null, "Book not found", HttpStatus.NOT_FOUND);
    }
    book = bookRepo.findById(book.getUid()).orElse(null);
    if (book == null) {
      return APIResponseFactory.buildErrorMessage(null, "Book not found", HttpStatus.NOT_FOUND);
    }

    if (user.getWishlistBooks().contains(book)) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book already on wishlist");
    }

    book.getUsersWishlists().add(user);
    user.getWishlistBooks().add(book);

    if (bookRepo.save(book) == null || userRepo.save(user) == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory.buildDefaultSuccesMessage(book, "Book added to wishlist.");
  }

  @DeleteMapping({"/admin/wishlist", "/api/wishlist"})
  public ResponseEntity removeBook(@RequestBody Book book, @RequestHeader("authorization") String token) {
    String userEmail = MiddlewareUtils.getEmailFromToken(token);
    User user = userRepo.findByEmail(userEmail).orElse(null);

    if (user == null || user.getUid() == null) {
      return APIResponseFactory.buildErrorMessage(null, "User not found", HttpStatus.NOT_FOUND);
    }

    if (book == null || book.getUid() == null) {
      return APIResponseFactory.buildErrorMessage(null, "Book not found", HttpStatus.NOT_FOUND);
    }
    book = bookRepo.findById(book.getUid()).orElse(null);
    if (book == null) {
      return APIResponseFactory.buildErrorMessage(null, "Book not found", HttpStatus.NOT_FOUND);
    }

    if (!user.getWishlistBooks().contains(book)) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book not on wishlist");
    }

    book.getUsersWishlists().remove(user);
    user.getWishlistBooks().remove(book);

    if (bookRepo.save(book) == null || userRepo.save(user) == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory.buildDefaultSuccesMessage(book,
        "Book removed from wishlist. Returning added book.");
  }
}