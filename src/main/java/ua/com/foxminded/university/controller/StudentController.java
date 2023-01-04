package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.univesity.exception.ServiceException;

@Slf4j
@Controller
public class StudentController {
    
    private StudentService<StudentModel> studentService;
    
    @Autowired
    public StudentController(StudentService<StudentModel> studentService) {
        this.studentService = studentService;
    }
    
    @RequestMapping(value = "/index", params = "getAllStudents")
    public String getAllStudents(Model model) {
        try {
            List<StudentModel> students = studentService.getAllStudents();
            model.addAttribute("students", students);
        } catch (ServiceException e) {
            log.error("Getting all students was failed", e);
        }
        return "students/list";
    }
}
