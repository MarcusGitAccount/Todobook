package com.bookstore.app.responsefactory;

import com.bookstore.app.controller.util.GenericJSONMessage;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APIResponseFactory {
  public static ResponseEntity buildSuccesMessage(Object body, String message, HttpStatus status) {
    GenericJSONMessage jsonMessage = GenericJSONMessage
        .builder()
        .status(status)
        .data(body).build();

    jsonMessage.addMessage(message);
    return ResponseEntity.status(status).body(jsonMessage);
  }

  public static ResponseEntity buildErrorMessage(Object body, String message, HttpStatus status) {
    GenericJSONMessage jsonErrorMessage = GenericJSONMessage
        .builder()
        .status(status)
        .data(body).build();

    jsonErrorMessage.addErrorMessage(message);
    return ResponseEntity.status(status).body(jsonErrorMessage);
  }

  public static ResponseEntity buildDefaultSuccesMessage(Object body, String message) {
    return buildSuccesMessage(body, message, HttpStatus.OK);
  }

  public static ResponseEntity buildDefaultErrorMessage(Object body, String message) {
    return buildErrorMessage(body, message, HttpStatus.BAD_REQUEST);
  }

  public static ResponseEntity buildMessageFromEntityValidation(ValidationMessage validationMessage) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    GenericJSONMessage jsonMessage = GenericJSONMessage
        .builder()
        .status(status)
        .data(null)
        .build();

    jsonMessage.getMessages().addAll(validationMessage.getMessages());
    jsonMessage.getErrorMessages().addAll(validationMessage.getErrorMessages());
    return ResponseEntity.status(status).body(jsonMessage);
  }
}