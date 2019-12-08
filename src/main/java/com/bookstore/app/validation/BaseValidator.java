package com.bookstore.app.validation;

import com.bookstore.app.validation.util.ValidationMessage;

public interface BaseValidator<T> {
  ValidationMessage validate(T t);
}
