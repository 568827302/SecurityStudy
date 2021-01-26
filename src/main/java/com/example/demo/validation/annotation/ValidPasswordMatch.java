package com.example.demo.validation.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.example.demo.validation.MyPasswordMatchValidator;

@Target({TYPE, ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyPasswordMatchValidator.class)
public @interface ValidPasswordMatch {

    String message() default "{PasswordMatches.userDto}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
