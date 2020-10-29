package com.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class NameValidator implements Validator {
    public void validate(String email, String password, String name) {
        System.out.println(name);
//        if (name == null || name.isBlank() || name.length() > 20) {
//            throw new IllegalArgumentException("invalid name:" + name);
//        }
    }
}
