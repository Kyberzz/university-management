package ua.com.foxminded.university.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    
    private final String fieldName;
    private final String message;
}
