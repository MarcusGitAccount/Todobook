package com.bookstore.app.controller.response.model;

import java.lang.reflect.Field;

public class ResponseModel<T> {
  protected ResponseModel(T base)  {
    Field[] fields = this.getClass().getDeclaredFields();

    for (Field field: fields) {
      try {
        Field baseField = base.getClass().getDeclaredField(field.getName());

        field.setAccessible(true);
        baseField.setAccessible(true);
        field.set(this, baseField.get(base));
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
  }
}
