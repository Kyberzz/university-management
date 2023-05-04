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
    
    public static final String SLASH = "/";
    public static final String LIST_TEMPLATE = "list";
    public static final String REDIRECT_KEY_WORD = "redirect:";
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public ModelAndView serviceExceptionHandler(HttpServletRequest request, ServiceException e) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("The controller class method fails.", e);
        modelAndView.addObject("url", request.getRequestURI());
        modelAndView.setViewName("error");
        return modelAndView;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView binding(HttpServletRequest request, BindException e) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("The binding request data fails", e);
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}