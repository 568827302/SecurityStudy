package com.example.demo.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.example.demo.validation.MyPasswordValidator;

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = MyPasswordValidator.class)
@Documented
public @interface ValidPassword {

    String message() default "{PasswordMatches.userDto}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
