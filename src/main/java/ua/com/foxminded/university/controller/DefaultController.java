package ua.com.foxminded.university.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;

@Slf4j
public class DefaultController {
  
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, ServiceException e) {
        ModelAndView modelAndView = new ModelAndView();
        log.error("The controller class method fails.", e);

        modelAndView.addObject("url", request.getRequestURI());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
