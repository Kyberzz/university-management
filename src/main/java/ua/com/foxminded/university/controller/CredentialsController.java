package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.AuthorityModel;
import ua.com.foxminded.university.service.AuthorityService;

@Slf4j
@Controller
public class CredentialsController {
    
    AuthorityService<AuthorityModel> credentialsService;
    
    @Autowired
    public CredentialsController(AuthorityService<AuthorityModel> credentialsService) {
        this.credentialsService = credentialsService;
    }

    @GetMapping("/authorization")
    public String authorize(Model model) {
        try {
            AuthorityModel credentials = credentialsService.getAllAuthorityKinds();
            model.addAttribute("credentials", credentials);
        } catch (ServiceException e) {
            log.error("Authorizing a new profile failed.");
        }
        return "authorization";
    }
    
    /*
    @PostMapping("/authorization")
    public String addAuthorizeProfile(Model model) {
        try {
            
            
        } catch (Exception e) {
            
        }
        
    }
    */
}
