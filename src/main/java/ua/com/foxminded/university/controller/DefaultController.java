package ua.com.foxminded.university.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ErrorResponse;
import ua.com.foxminded.university.exception.ErrorCode;
import ua.com.foxminded.university.exception.ServiceException;

@Slf4j
public class DefaultController {
    
    public static final String NOT_NULL_TEACHER_CONSTRAINT = "lessons_teacher_id_fkey";
    public static final String AMPERSAND_SIGN = "&";
    public static final int STUB = 0;
    public static final String EQUAL_SIGN = "=";
    public static final String ERRORS_RESPONSE_ATTRIBUTE = "errorsResponse";
    public static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    public static final String QUESTION_MARK = "?";
    public static final String SLASH = "/";
    public static final String REDIRECT_KEY_WORD = "redirect:";
    public static final String URL_ATTRIBUTE = "url";
    public static final String ERROR_TEMPLATE_NAME = "error";
    
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class) 
    public ModelAndView handleRuntimeException(HttpServletRequest request, 
                                               HttpServletResponse response, 
                                               RuntimeException exception) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("API error", exception);
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURI());
        modelAndView.addObject(ERROR_MESSAGE_ATTRIBUTE, exception.getMessage());
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class) 
    public ModelAndView handleDataIntegrityViolation(HttpServletRequest request, 
            HttpServletResponse response, 
            DataIntegrityViolationException ex) {
        log.error("API error", ex);
        
        ErrorCode errorCode;
        String constraintName = ((org.hibernate.exception.ConstraintViolationException)ex
                .getCause()).getConstraintName();
        
        if (constraintName.equals(NOT_NULL_TEACHER_CONSTRAINT)) {
            errorCode = ErrorCode.TEACHER_NOT_NULL_CONSTRAINT_VIOLATION;
        } else {
            errorCode = ErrorCode.API_ERROR;
        }
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURI());
        modelAndView.addObject(ERROR_MESSAGE_ATTRIBUTE, 
                errorCode.getDescription());
        response.setStatus(errorCode.getCode());
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
    
    @ExceptionHandler(ServiceException.class)
    public ModelAndView serviceExceptionHandler(HttpServletRequest request, 
                                                HttpServletResponse response, 
                                                ServiceException error) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("The service error has occurred", error);
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURI());
        modelAndView.addObject(ERROR_MESSAGE_ATTRIBUTE, error.getErrorCode().getDescription());
        response.setStatus(error.getErrorCode().getCode());
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleModelAttributeFieldValueViolation(
            HttpServletRequest request, ConstraintViolationException error) {
        
        ModelAndView modelAndView = new ModelAndView();
        List<ErrorResponse> errorsResponse = new ArrayList<>();
        
        for (ConstraintViolation<?> violation : error.getConstraintViolations()) {
            errorsResponse.add(new ErrorResponse(violation.getPropertyPath().toString(), 
                                                 violation.getMessage()));
        }
        
        log.error("The argument value violation of the controller method was occured");
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURI());
        modelAndView.addObject(ERRORS_RESPONSE_ATTRIBUTE, errorsResponse);
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodArgumanViolation(HttpServletRequest request, 
                                                     MethodArgumentNotValidException error) {
        ModelAndView modelAndView = new ModelAndView();
        List<ErrorResponse> errorsResponse = getErrorsResponse(error);
        log.error("The argument violation of the controller method was occured");
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURI());
        modelAndView.addObject(ERRORS_RESPONSE_ATTRIBUTE, errorsResponse);
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView binding(HttpServletRequest request, BindException error) {
        List<ErrorResponse> errorsResponse = getErrorsResponse(error);
        
        ModelAndView modelAndView = new ModelAndView();
        log.error("The request data binding fails", error);
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURL());
        modelAndView.addObject(ERRORS_RESPONSE_ATTRIBUTE, errorsResponse);
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
    
    private List<ErrorResponse>  getErrorsResponse(BindException error) {
        return  error.getBindingResult()
                     .getFieldErrors().stream().map(fieldError -> new ErrorResponse(
                             fieldError.getField(),
                             fieldError.getDefaultMessage()))
                     .collect(Collectors.toList());
    }
}
