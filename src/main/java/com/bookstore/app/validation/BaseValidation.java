package com.bookstore.app.validation;

import com.bookstore.app.validation.util.ValidationMessage;

public interface BaseValidation<T> {
  ValidationMessage validate(T t);
}
