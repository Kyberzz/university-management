package ua.com.foxminded.university.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Service;

@Service
public class ValidatorServiceImpl<T> implements ValidatorService<T> {

    @Override
    public void validate(T model) throws ConstraintViolationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(model);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
