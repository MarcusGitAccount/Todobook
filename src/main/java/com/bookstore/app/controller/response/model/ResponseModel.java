package com.bookstore.app.controller.response.model;

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;

@Slf4j
class ResponseModel<T> {

  ResponseModel(T base)  {
    Field[] fields = this.getClass().getDeclaredFields();

    for (Field field: fields) {
      try {
        Field baseField = base.getClass().getDeclaredField(field.getName());

        field.setAccessible(true);
        baseField.setAccessible(true);
        field.set(this, baseField.get(base));
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    }
  }

}
