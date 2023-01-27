package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.StudentService;

@Controller
public class StudentController extends DefaultController {

    private StudentService<StudentModel> studentService;

    @Autowired
    public StudentController(StudentService<StudentModel> studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("/students/list")
    public String getAllStudents(Model model) throws ServiceException {
        List<StudentModel> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students/list";
    }
}
