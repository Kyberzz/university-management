package ua.com.foxminded.university.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.service.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends DefaultController {
    
    public static final String USER_ID_PARAMETER = "userId";
    public static final String NOT_FOUND_USER_TEMPLATE_PATH = "users/not-found";
    public static final String NO_CONFRIM_TEMPLATE_PATH = "users/no-confirm";
    public static final String CONFIRMATION_PASSWORD_PARAMETER = "confirmationPassword";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String EMAIL_PARAMETER = "email";
    public static final String NOT_AUTHORIZED_USERS_ATTRIBUTE = "notAuthorizedUsers";
    public static final String USERS_LIST_TEMPLATE_PATH = "users/list";
    public static final String USERS_ATTRIBUTE = "users";
    public static final String USER_ATTRIBUTE = "user";
    
    private final UserService userService;
    
    @PostMapping("/{userId}/delete")
    public String delete(@PathVariable int userId) {
        userService.deleteById(userId);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(USERS_LIST_TEMPLATE_PATH)
                                  .toString();
    }
    
    @PostMapping("/{userId}/edit-email")
    public String editEmail(@PathVariable int userId, 
                            @ModelAttribute UserDTO user) {
        
        userService.updateEmail(userId, user.getEmail());
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(USERS_LIST_TEMPLATE_PATH)
                                  .toString();
    }
    
    @PostMapping("/create-user-person")
    public String createPerson(@ModelAttribute(USER_ATTRIBUTE) UserDTO user) {
        UserDTO createdUser = userService.createUserPerson(user);
        
        if (user.hasEmail()) {
            userService.updateEmail(createdUser.getId(), user.getEmail());
        }
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(USERS_LIST_TEMPLATE_PATH)
                                  .toString();
    }
    
    @PostMapping(value = "/delete", params = "email")
    public String deleteAuthority(@RequestParam String email) {
        userService.deleteByEmail(email);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(USERS_LIST_TEMPLATE_PATH)
                                  .toString();
    }
    
    @PostMapping(value = "/edit")
    public String update(@RequestParam(USER_ID_PARAMETER) Integer userId, 
                         @Valid @ModelAttribute(USER_ATTRIBUTE) UserDTO user) {

        UserDTO persistedUser = userService.getById(userId);
        persistedUser.setEnabled(user.getEnabled());
        persistedUser.setUserAuthority(user.getUserAuthority());
        userService.updateUser(persistedUser);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(USERS_LIST_TEMPLATE_PATH)
                                  .toString();
    }

    @PostMapping("/authorize")
    public String authorize(@RequestParam(EMAIL_PARAMETER) String email,
                            @RequestParam(PASSWORD_PARAMETER) String password, 
                            @RequestParam(CONFIRMATION_PASSWORD_PARAMETER) String passwordConfirm,
                            @Valid @ModelAttribute (USER_ATTRIBUTE) UserDTO user) {
        
        if (!password.equals(passwordConfirm)) {
            return NO_CONFRIM_TEMPLATE_PATH;
        }
        
        try {
            userService.getByEmail(email);
        } catch (ServiceException e) {
            return NOT_FOUND_USER_TEMPLATE_PATH;
        }
        
        user.setPassword(password);
        userService.updateUser(user);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(USERS_LIST_TEMPLATE_PATH)
                                  .toString();
    }
    
    @GetMapping("/list")
    public String getAll(Model model) {
        List<UserDTO> users = userService.getAll();
        List<UserDTO> notAuthorizedUsers = userService.getNotAuthorizedUsers();
        model.addAttribute(NOT_AUTHORIZED_USERS_ATTRIBUTE, notAuthorizedUsers);
        model.addAttribute(USERS_ATTRIBUTE, users);
        model.addAttribute(USER_ATTRIBUTE, new UserDTO());
        return USERS_LIST_TEMPLATE_PATH;
    }
}
