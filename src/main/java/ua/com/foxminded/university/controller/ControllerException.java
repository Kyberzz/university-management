package ua.com.foxminded.university.controller;

public class ControllerException extends Exception {

    private static final long serialVersionUID = 1L;

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
