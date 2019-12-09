package com.bookstore.app.validation;

import com.bookstore.app.entity.User;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserValidation implements BaseValidation<User> {

  @Autowired
  private UserRepo userRepo;

  // password check too
  public ValidationMessage validateSignup(User user) {
    ValidationMessage validationMessage = ValidationMessage.builder().isValid(true).build();
    User existing = userRepo.findByEmail(user.getEmail()).orElse(null);
    boolean isValid = true;

    if (existing != null) {
      validationMessage.addErrorMessage("Email address already taken.").setIsValid(false);
    }

    ValidationMessage baseValidation = validate(user);
    if (!baseValidation.getIsValid()) {
      validationMessage.merge(baseValidation);
    }

    return validationMessage;
  }

  // general validation for user alteration, not creation
  public ValidationMessage validate(User user) {
    return ValidationMessage.builder().isValid(true).build();
  }
}
