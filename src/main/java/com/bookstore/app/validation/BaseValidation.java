package com.bookstore.app.validation;

import com.bookstore.app.validation.util.ValidationMessage;

public interface BaseValidation<T> {
  ValidationMessage validate(T t);

  static void nullCheck(Object o, String err, ValidationMessage message) {
      if (o == null) {
        message.addErrorMessage(err);
        message.setIsValid(false);
      }
  }

  static boolean isNull(Object o) {
    return o == null;
  }
}
