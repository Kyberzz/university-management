package ua.com.foxminded.university.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.service.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends DefaultController {
    
    public static final String USERS_PATH = "/users/";
    
    private final UserService userService;

    @PostMapping(value = "/delete", params = "email")
    public String delete(@RequestParam String email) throws ServiceException {
        userService.deleteByEmail(email);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(USERS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
    
    @PostMapping(value = "/edit", params = {"userId"})
    public String update(@RequestParam("userId") Integer userId, 
                         @Valid @ModelAttribute UserModel userModel, 
                         BindingResult bindingResult) throws ServiceException, 
                                                             BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        UserModel persistedUser = userService.getById(userId);
        persistedUser.setEnabled(userModel.getEnabled());
        persistedUser.setUserAuthority(userModel.getUserAuthority());
        userService.update(persistedUser);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(USERS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }

    @GetMapping("/list")
    public String listAll(Model model) throws ServiceException {
        List<UserModel> allUsers = userService.getAll();
        List<UserModel> notAuthorizedUsers = userService.getNotAuthorizedUsers();
        model.addAttribute("notAuthorizedUsers", notAuthorizedUsers);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("userModel", new UserModel());
        return "users/list";
    }

    @PostMapping(value = "/authorize", params = {"email", "password", "passwordConfirm"})
    public String authorize(@RequestParam("email") String email,
                            @RequestParam("password") String password, 
                            @RequestParam("passwordConfirm") String passwordConfirm,
                            @Valid @ModelAttribute UserModel userModel, 
                            BindingResult bindingResult) throws ServiceException, 
                                                                BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        if (!password.equals(passwordConfirm)) {
            return "users/no-confirm";
        }
        
        try {
            userService.getByEmail(email);
        } catch (ServiceException e) {
            return "users/not-found";
        }
        
        userModel.setPassword(password);
        userService.update(userModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(USERS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
}
