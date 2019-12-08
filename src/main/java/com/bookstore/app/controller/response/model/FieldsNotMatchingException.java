package com.bookstore.app.controller.response.model;

public class FieldsNotMatchingException extends Exception {
  public FieldsNotMatchingException() {
    super("Fields do not match between base model and response model.");
  }

  public FieldsNotMatchingException(String message) {
    super(message);
  }
}
