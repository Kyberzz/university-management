package ua.com.foxminded.university.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ErrorResponse;
import ua.com.foxminded.university.exception.ServiceException;

@Slf4j
public class DefaultController {
    
    public static final String ERRORS_RESPONCE = "errorsResponse";
    public static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    public static final String QUESTION_MARK = "?";
    public static final String SLASH = "/";
    public static final String LIST_TEMPLATE = "list";
    public static final String REDIRECT_KEY_WORD = "redirect:";
    public static final String URL_ATTRIBUTE = "url";
    public static final String ERROR_TEMPLATE_NAME = "error";
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ModelAndView serviceExceptionHandler(HttpServletRequest request, 
                                                ServiceException error) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("The service error has occurred", error);
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURI());
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        modelAndView.addObject(ERROR_MESSAGE_ATTRIBUTE, error.getMessage());
        return modelAndView;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ModelAndView binding(HttpServletRequest request, BindException error) {
        List<ErrorResponse> errorsResponse = getErrorsResponse(error);
        
        ModelAndView modelAndView = new ModelAndView();
        log.error("The request data binding fails", error);
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURL());
        modelAndView.addObject(ERRORS_RESPONCE, errorsResponse);
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
