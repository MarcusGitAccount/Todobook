package com.bookstore.app.controller.advice;

import com.bookstore.app.responsefactory.APIResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleBasicException(Exception ex) {
    return APIResponseFactory.buildDefaultErrorMessage(null, ex.getMessage());
  }
}
