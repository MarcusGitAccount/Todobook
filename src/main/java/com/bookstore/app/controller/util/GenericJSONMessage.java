package com.bookstore.app.controller.util;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
public class GenericJSONMessage {
  private final Object data;

  private final List<String> messages = new LinkedList<>();

  private final List<String> errorMessages = new LinkedList<>();

  private final HttpStatus status;

  public GenericJSONMessage addMessage(String message) {
    this.messages.add(message);
    return this;
  }

  public GenericJSONMessage addErrorMessage(String errorMessage) {
    this.errorMessages.add(errorMessage);
    return this;
  }
}
