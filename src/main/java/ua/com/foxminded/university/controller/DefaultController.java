package ua.com.foxminded.university.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;

@Slf4j
public class DefaultController {
    
    public static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
    public static final String QUESTION_MARK = "?";
    public static final String SLASH = "/";
    public static final String LIST_TEMPLATE = "list";
    public static final String REDIRECT_KEY_WORD = "redirect:";
    public static final String URL_ATTRIBUTE = "url";
    public static final String ERROR_TEMPLATE_NAME = "error";
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public ModelAndView serviceExceptionHandler(HttpServletRequest request, ServiceException e) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("The service error has occurred", e);
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURI());
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView binding(HttpServletRequest request, BindException e) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("The request data binding fails", e);
        modelAndView.addObject(URL_ATTRIBUTE, request.getRequestURL());
        modelAndView.addObject(ERROR_MESSAGE_ATTRIBUTE, e.getMessage());
        modelAndView.setViewName(ERROR_TEMPLATE_NAME);
        return modelAndView;
    }
}
