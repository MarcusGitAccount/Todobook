package com.bookstore.app.controller;

import com.bookstore.app.controller.request.model.LoanRequest;
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
public class LoanController {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private BookRepo bookRepo;

  @PostMapping({"/admin/loan", "/api/loan"})
  public ResponseEntity createLoan(@RequestBody Book book,
                                   @RequestHeader("authorization") String token) {
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

    if (!book.getIsAvailableToLoan() || user.getLoans().contains(book)) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Book not available to loan");
    }

    book.setIsAvailableToLoan(false);
    book.setLoanedTo(user);
    user.getLoans().add(book);

    if (bookRepo.save(book) == null ||
        userRepo.save(user) == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory.buildDefaultSuccesMessage(book, "Loan created. Returning added book.");
  }

  @DeleteMapping({"/admin/loan", "/api/loan"})
  public ResponseEntity cancelLoan(@RequestBody Book book,
                                   @RequestHeader("authorization") String token) {
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

    if (book.getIsAvailableToLoan() || !user.getLoans().contains(book)) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Cannot cancel loan. It might not even exist.");
    }

    book.setIsAvailableToLoan(true);
    book.setLoanedTo(null);
    user.getLoans().remove(book);

    if (bookRepo.save(book) == null ||
        userRepo.save(user) == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory.buildDefaultSuccesMessage(book, "Loan canceled. Returning the book as data.");
  }
}
