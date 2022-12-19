package ua.com.foxminded.university.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.buisness.model.StudentModel;
import ua.com.foxminded.university.buisness.model.service.ServiceException;
import ua.com.foxminded.university.buisness.model.service.StudentService;

@Slf4j
@Controller
public class UniversityManagementController {
    
    private StudentService<StudentModel> studentService;
    
    @Autowired
    public UniversityManagementController(StudentService<StudentModel> studentService) {
        this.studentService = studentService;
    }
    
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    
}
