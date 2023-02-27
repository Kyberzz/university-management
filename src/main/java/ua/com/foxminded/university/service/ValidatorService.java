package ua.com.foxminded.university.service;

import javax.validation.ConstraintViolationException;

public interface ValidatorService<T> {
    
    public void validate(T model) throws ConstraintViolationException;
}
