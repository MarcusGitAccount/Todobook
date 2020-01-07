package com.bookstore.app.validation;

import com.bookstore.app.entity.User;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.validation.util.ValidationMessage;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserValidation implements BaseValidation<User> {

  @Autowired
  private UserRepo userRepo;

  private static final String PASSWORD_MATCHER =
      "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$";

  private static final String PASSWRD_REQUIREMENTS =
      "Password should have at least 8 characters and contain at least one uppercase and lowercase letter, one digit, and one special character." ;

  // password check too
  public ValidationMessage validateSignup(User user) {
    ValidationMessage validationMessage = ValidationMessage.builder().isValid(true).build();
    User existing = userRepo.findByEmail(user.getEmail()).orElse(null);
    boolean isValid = true;

    if (existing != null) {
      validationMessage.addErrorMessage("Email address already taken.");
    }

    BaseValidation.nullCheck(user.getEmail(), "Email field empty", validationMessage);
    BaseValidation.nullCheck(user.getPassword(), "Password field empty", validationMessage);

    if (!user.getPassword().matches(PASSWORD_MATCHER)) {
      validationMessage.addErrorMessage(PASSWRD_REQUIREMENTS);
    }

    ValidationMessage baseValidation = validate(user);
    if (!baseValidation.getIsValid()) {
      validationMessage.merge(baseValidation);
    }

    return validationMessage;
  }

  // general validation for user alteration, not creation
  @Override
  public ValidationMessage validate(User user) {
    ValidationMessage validationMessage = ValidationMessage.builder().isValid(true).build();

    if (!EmailValidator.getInstance().isValid(user.getEmail())) {
      validationMessage.addErrorMessage("Invalid email.");
    }

    if (!BaseValidation.isNull(user.getPhoneNumber())) {
      if (!user.getPhoneNumber().matches("[0-9]{10}")) {
        validationMessage.addErrorMessage("Phone number should be only 10 digits.");
      }
    }

    if (!BaseValidation.isNull(user.getFirstName())) {
      if (!user.getFirstName().matches("[A-Za-z]{1,20}")) {
        validationMessage.addErrorMessage("First name should contain at max 20 letters.");
      }
    }

    if (!BaseValidation.isNull(user.getLastName())) {
      if (!user.getLastName().matches("[A-Za-z]{1,20}")) {
        validationMessage.addErrorMessage("Last name should contain at max 20 letters.");
      }
    }

    return validationMessage;
  }
}
