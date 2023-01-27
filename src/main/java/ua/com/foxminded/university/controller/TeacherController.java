package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.TeacherService;

@Controller
public class TeacherController extends DefaultController {

    private TeacherService<TeacherModel> teacherService;

    @Autowired
    public TeacherController(TeacherService<TeacherModel> teacherService) {
        this.teacherService = teacherService;
    }

    @RequestMapping(value = "/teachers/list")
    public String getAllTeachers(Model model) throws ServiceException {
        List<TeacherModel> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        return "teachers/list";
    }
}
