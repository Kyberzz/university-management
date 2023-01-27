package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.service.UserService;

@Controller
public class UserController extends DefaultController {

    UserService<UserModel> userService;

    @Autowired
    public UserController(UserService<UserModel> userService) {
        this.userService = userService;
    }

    @GetMapping("/authorization")
    public String authorize(Model model) {
        UserModel userModel = new UserModel();
        userModel.setIsActive(true);
        model.addAttribute("user", userModel);
        return "authorization";
    }

    /*
     * @PostMapping("/authorization") public String addAuthorizeProfile(Model model)
     * { try {
     * 
     * 
     * } catch (Exception e) {
     * 
     * } }
     */
}
