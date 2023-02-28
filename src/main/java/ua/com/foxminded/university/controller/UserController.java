package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController extends DefaultController {
    
    private static final String DEFAULT_PASSWORD = "{noop}pass";

    private UserService<UserModel> userService;

    public UserController(UserService<UserModel> userService) {
        this.userService = userService;
    }
    
    @PostMapping(value = "/delete", params = {"email"})
    public String delete(@RequestParam("email") String email) throws ServiceException {
        userService.deleteByEmail(email);
        return "redirect:/users/list";
    }
    
    @PostMapping(value = "/edit", params = {"userId"})
    public String edit(@RequestParam("userId") Integer userId, 
                       UserModel updatedUser, 
                       BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            handleBindingResultError(bindingResult);
        }
        
        UserModel persistedUser = userService.getById(userId);
        persistedUser.setEnabled(updatedUser.getEnabled());
        
        if (persistedUser.getPassword() == null) {
            persistedUser.setPassword(DEFAULT_PASSWORD);
        }
        
        if (updatedUser.hasUserAuthority()) {
            if (persistedUser.hasUserAuthority()) {
                Integer userAuthorityId = persistedUser.getUserAuthority().getId();
                updatedUser.getUserAuthority().setId(userAuthorityId);
            }
            updatedUser.getUserAuthority().setUser(persistedUser);
            persistedUser.setUserAuthority(updatedUser.getUserAuthority());
        }
        userService.updateUser(persistedUser);
        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String listAllUsers(Model model) throws ServiceException {
        List<UserModel> allUsers = userService.getAll();
        List<UserModel> notAuthorizedUsers = userService.getNotAuthorizedUsers();
        UserModel modelUser = new UserModel();
        model.addAttribute("notAuthorizedUsers", notAuthorizedUsers);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("userModel", modelUser);
        return "users/list";
    }

    @PostMapping(value = "/authorize", params = {"password", "passwordConfirm"})
    public String authorizeUser(@RequestParam("email") String email,
                                @RequestParam("password") String password, 
                                @RequestParam("passwordConfirm") String passwordConfirm,
                                UserModel userModel, 
                                BindingResult bindingResult) throws ServiceException {
        handleBindingResultError(bindingResult);

        if (!password.equals(passwordConfirm)) {
            return "users/noconfirm";
        }
        
        try {
            userService.getByEmail(email);
        } catch (ServiceException e) {
            return "users/notfound";
        }
        
        userModel.setPassword(password);
        userService.createUser(userModel);
        return "redirect:/users/list";
    }
}
