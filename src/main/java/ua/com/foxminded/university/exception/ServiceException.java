package ua.com.foxminded.university.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private ErrorCode errorCode;
    
    public ServiceException() {
        super();
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(ErrorCode userCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = userCode;
    }

    public ServiceException(ErrorCode userCode, Throwable cause) {
        this(userCode, cause.getMessage(), cause);
    }
}
