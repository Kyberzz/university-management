package ua.com.foxminded.university.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.service.UserService;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController extends DefaultController {

    UserService<UserModel> userService;

    @Autowired
    public UserController(UserService<UserModel> userService) {
        this.userService = userService;
    }
    
    @GetMapping("/list")
    public String listAllUsers(Model model) throws ServiceException {
        List<UserModel> users = userService.getAllUsers();
        UserModel authorizedUser = new UserModel();
        model.addAttribute("users", users);
        model.addAttribute("userModel", authorizedUser);
        return "users/list";
    }

    @PostMapping(value = "/authorize", params = {"password", "passwordConfirm"})
    public String authorize(@RequestParam("password") String password, 
                            @RequestParam("passwordConfirm") String passwordConfirm,
                            UserModel userModel, 
                            BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream()
                                        .forEach(error -> log.error(error.getDefaultMessage()));
            return "error";
        }

        if (!password.equals(passwordConfirm)) {
            return "users/nonconfirm";
            
        }
        userModel.setPassword(password);
        userService.udateUser(userModel);
        
        return "redirect:/users/list";
    }
}
