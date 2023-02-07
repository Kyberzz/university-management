package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.service.UserAuthorityService;
import ua.com.foxminded.university.service.UserService;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController extends DefaultController {

    UserService<UserModel> userService;
    UserAuthorityService<UserAuthorityModel> userAuthotiryService;

    @Autowired
    public UserController(UserService<UserModel> userService,
            UserAuthorityService<UserAuthorityModel> userAuthotiryService) {
        this.userService = userService;
        this.userAuthotiryService = userAuthotiryService;
    }

    @GetMapping("/list")
    public String listAllUsers(Model model) throws ServiceException {
        List<UserModel> allUsers = userService.getAllUsers();
        List<UserModel> notAuthorizedUsers = userService.getNotAuthorizedUsers();
        UserModel newUser = new UserModel();
        model.addAttribute("notAuthorizedUsers", notAuthorizedUsers);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("updatedUser", newUser);
        return "users/list";
    }

    @PostMapping(value = "/authorize", params = {"password", "passwordConfirm"})
    public String authorizeUser(@RequestParam("email") String email,
                                @RequestParam("password") String password, 
                                @RequestParam("passwordConfirm") String passwordConfirm,
                                UserModel updatedUser, 
                                BindingResult bindingResult) throws ServiceException {
        handleBindingResultError(bindingResult);

        if (!password.equals(passwordConfirm)) {
            return "users/noconfirm";
        }
        
        UserModel persistedUser = null;
        
        try {
            persistedUser = userService.getByEmail(email);
        } catch (ServiceException e) {
            return "users/notfound";
        }

        persistedUser.setEmail(updatedUser.getEmail());
        persistedUser.setPassword(password);
        persistedUser.setIsActive(updatedUser.getIsActive());

        UserAuthorityModel userAuthority = updatedUser.getUserAuthority();
        userAuthority.setUser(persistedUser);
        persistedUser.setUserAuthority(userAuthority);
        userService.updateUser(persistedUser);
        return "redirect:/users/list";
    }
    
    private String handleBindingResultError(BindingResult bindingResult) {
        bindingResult.getAllErrors()
                     .stream()
                     .forEach(error -> log.error(error.getDefaultMessage()));
        return "error";
    }
}
