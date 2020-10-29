package com.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class PasswordValidator implements Validator {
    public void validate(String email, String password, String name) {
        System.out.println(password);
//        if (!password.matches("^.{6,20}$")) {
//            throw new IllegalArgumentException("invalid password");
//        }
    }
}
