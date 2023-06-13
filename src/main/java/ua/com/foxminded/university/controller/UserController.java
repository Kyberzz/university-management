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
    
    public static final String USERS_LIST_TEMPLATE_PATH = "users/list";
    public static final String USERS_ATTRIBUTE = "users";
    public static final String USER_ATTRIBUTE = "user";
    public static final String USERS_PATH = "/users/";
    
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
    public String createPerson(@ModelAttribute UserDTO user) {
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
                                  .append(USERS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
    
    @PostMapping(value = "/edit", params = {"userId"})
    public String update(@RequestParam("userId") Integer userId, 
                         @Valid @ModelAttribute UserDTO userModel) {

        UserDTO persistedUser = userService.getById(userId);
        persistedUser.setEnabled(userModel.getEnabled());
        persistedUser.setUserAuthority(userModel.getUserAuthority());
        userService.updateUser(persistedUser);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(USERS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        List<UserDTO> users = userService.getAll();
        List<UserDTO> notAuthorizedUsers = userService.getNotAuthorizedUsers();
        model.addAttribute("notAuthorizedUsers", notAuthorizedUsers);
        model.addAttribute(USERS_ATTRIBUTE, users);
        model.addAttribute(USER_ATTRIBUTE, new UserDTO());
        return USERS_LIST_TEMPLATE_PATH;
    }

    @PostMapping(value = "/authorize", params = {"email", "password", "passwordConfirm"})
    public String authorize(@RequestParam("email") String email,
                            @RequestParam("password") String password, 
                            @RequestParam("passwordConfirm") String passwordConfirm,
                            @Valid @ModelAttribute UserDTO userModel) {
        if (!password.equals(passwordConfirm)) {
            return "users/no-confirm";
        }
        
        try {
            userService.getByEmail(email);
        } catch (ServiceException e) {
            return "users/not-found";
        }
        
        userModel.setPassword(password);
        userService.updateUser(userModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(USERS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
}
