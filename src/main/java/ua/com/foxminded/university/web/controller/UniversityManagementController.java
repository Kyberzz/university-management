package ua.com.foxminded.university.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.buisness.model.StudentModel;
import ua.com.foxminded.university.buisness.model.TeacherModel;
import ua.com.foxminded.university.buisness.model.service.ServiceException;
import ua.com.foxminded.university.buisness.model.service.StudentService;
import ua.com.foxminded.university.buisness.model.service.TeacherService;

@Slf4j
@Controller
public class UniversityManagementController {
    
    private StudentService<StudentModel> studentService;
    private TeacherService<TeacherModel> teacherService;
    
    @Autowired
    public UniversityManagementController(StudentService<StudentModel> studentService) {
        this.studentService = studentService;
    }
    
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/index", params = "getAllStudents")
    public String getAllStudents(Model model) {
        try {
            List<StudentModel> students = studentService.getAllStudents();
            model.addAttribute("students", students);
        } catch (ServiceException e){
            log.error("Getting students was failed", e);
        }
        return "students";
    }
}
