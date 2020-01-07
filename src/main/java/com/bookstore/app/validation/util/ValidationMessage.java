package com.bookstore.app.validation.util;

import lombok.Builder;
import lombok.Data;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class ValidationMessage {
  private Boolean isValid;

  private final List<String> errorMessages = new LinkedList<>();

  private final List<String> messages = new LinkedList<>();

  public ValidationMessage addErrorMessage(String errorMessage) {
    this.errorMessages.add(errorMessage);
    this.setIsValid(false);

    return this;
  }

  public ValidationMessage addMessage(String message) {
    this.errorMessages.add(message);
    return this;
  }

  public ValidationMessage merge(ValidationMessage other) {
    this.messages.addAll(other.messages);
    this.errorMessages.addAll(other.errorMessages);
    this.isValid &= other.isValid;
    return this;
  }
}
