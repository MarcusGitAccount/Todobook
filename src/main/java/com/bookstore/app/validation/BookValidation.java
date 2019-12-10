package com.bookstore.app.validation;

import com.bookstore.app.entity.Book;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BookValidation implements BaseValidation<Book> {

  @Override
  public ValidationMessage validate(Book book) {
    return ValidationMessage.builder().isValid(true).build();
  }
}
