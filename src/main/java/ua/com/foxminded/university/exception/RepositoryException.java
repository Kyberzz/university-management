package ua.com.foxminded.university.exception;

public class RepositoryException extends Exception {

    private static final long serialVersionUID = 1L;

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
