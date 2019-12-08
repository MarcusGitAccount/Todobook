package com.bookstore.app.validation;

import com.bookstore.app.entity.Company;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CompanyValidation implements BaseValidator<Company> {

  @Override
  public ValidationMessage validate(Company company) {
    return ValidationMessage.builder().isValid(true).build();
  }
}
