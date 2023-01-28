package ua.com.foxminded.university.controller;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;

@Slf4j
public class DefaultController {
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void defaultExceptionHandler(Exception e) {
        log.error("The exception during execution of the program", e);
        ModelAndView modelAndView = new ModelAndView();
      //  modelAndView.addObject("url", request.getURI());
        modelAndView.setViewName("error");
    }
}
