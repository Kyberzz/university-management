package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.TeacherService;

@Slf4j
@Controller
public class TeacherController extends DefaultController {
    
    private TeacherService<TeacherModel> teacherService;
    
    @Autowired
    public TeacherController(TeacherService<TeacherModel> teacherService) {
        this.teacherService = teacherService;
    }
    
    @RequestMapping(value = "/teachers/list")
    public String getAllTeachers(Model model) {
        try {
            List<TeacherModel> teachers = teacherService.getAllTeachers();
            model.addAttribute("teachers", teachers);
        } catch (ServiceException e) {
            log.error("Getting all teachers was failed", e);
        }
        return "teachers/list";
    }
}
