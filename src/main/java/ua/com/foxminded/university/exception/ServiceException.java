package ua.com.foxminded.university.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private ServiceErrorCode errorCode;
    
    public ServiceException() {
        super();
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(ServiceErrorCode userCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = userCode;
    }

    public ServiceException(ServiceErrorCode userCode, Throwable cause) {
        this(userCode, cause.getMessage(), cause);
    }
}
