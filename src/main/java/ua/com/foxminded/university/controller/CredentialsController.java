package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CredentialsModel;
import ua.com.foxminded.university.service.CredentialsService;

@Slf4j
@Controller
public class CredentialsController {
    
    CredentialsService<CredentialsModel> credentialsService;
    
    @Autowired
    public CredentialsController(CredentialsService<CredentialsModel> credentialsService) {
        this.credentialsService = credentialsService;
    }

    @RequestMapping("/authorization")
    public String authorize(Model model) {
        try {
      //      List<CredentialsModel> credentialsList = credentialsService.getAll();
            model.addAttribute("credentialsList", credentialsList);
        } catch (ServiceException e) {
            log.error("Authorizing a new profile failed.");
        }
        return "authorization";
    }
}
