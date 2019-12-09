package com.bookstore.app.validation;

import com.bookstore.app.entity.Author;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AuthorValidation implements BaseValidation<Author> {

  @Override
  public ValidationMessage validate(Author author) {
    return ValidationMessage.builder().isValid(true).build();
  }
}
