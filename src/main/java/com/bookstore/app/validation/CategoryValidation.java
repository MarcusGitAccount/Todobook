package com.bookstore.app.validation;

import com.bookstore.app.entity.Category;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CategoryValidation implements BaseValidation<Category> {

  @Override
  public ValidationMessage validate(Category category) {
    return ValidationMessage.builder().isValid(true).build();
  }

}