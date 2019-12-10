package com.bookstore.app.validation;

import com.bookstore.app.entity.Comment;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CommentValidation implements BaseValidation<Comment> {
  @Override
  public ValidationMessage validate(Comment comment) {
    return ValidationMessage.builder().isValid(true).build();
  }
}
