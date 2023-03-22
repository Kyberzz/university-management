package ua.com.foxminded.university.service.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.service.ValidatorService;

@Service
@RequiredArgsConstructor
public class ValidatorServiceImpl<T> implements ValidatorService<T> {
    
    private final Validator validator; 

    @Override
    public void validate(T model) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> violations = validator.validate(model);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
