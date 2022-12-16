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

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.buisness.model.StudentModel;
import ua.com.foxminded.university.buisness.model.service.ServiceException;
import ua.com.foxminded.university.buisness.model.service.StudentService;

//@Slf4j
@Controller
//@ResponseBody
//@RequestMapping("users/")
public class StudentController {
    
    private StudentService<StudentModel> studentService;
    
    @Autowired
    public StudentController(StudentService<StudentModel> studentService) {
        this.studentService = studentService;
    }
    
    @RequestMapping("/")
    public String index(Model model) {
        StudentModel student = new StudentModel();
        student.setFirstName("Jon");
        student.setLastName("Snow");
        model.addAttribute(student);
        return "index";
    }
    
    /*
    @GetMapping("/students")
    public String getAllStudents(Model model) throws ServiceException {
        List<StudentModel> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return re
        
        
    }
    */
    
/*
    @ModelAttribute("allStudents")
    public List<StudentModel> populateStudents() throws ControllerException {
        try {
            return studentService.getAllStudents();
        } catch (ServiceException e) {
            throw new ControllerException("Getting all students fails", e);
        }
    }
    
    @RequestMapping("/student")
    public String showStudent(StudentModel student) {
        return "student";
    }
    
    @RequestMapping(value = "/student", params= {"save"})
    public String saveStudent(StudentModel student, BindingResult bindingResult, ModelMap model) 
            throws ControllerException {
       
        if (bindingResult.hasErrors()) {
            return "student";
        } else {
            try {
                studentService.addStudent(student);
            } catch (ServiceException e) {
                throw new ControllerException("The data student was not saved", e);
            }
            
            model.clear();
            return "redirect:/student";
        }
    }
    
    */
}
