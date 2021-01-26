package com.example.demo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.validation.annotation.ValidPasswordMatch;

public class MyPasswordMatchValidator implements ConstraintValidator<ValidPasswordMatch, UserDto> {

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
        return userDto.getPassword().equals(userDto.getMatchPassword());
    }
    
}
