package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.StudentService;

@Slf4j
@Controller
@ResponseBody
//@RequestMapping("users/")
public class StudentController {
    
    private StudentService studentService;
    
    //@RequestMapping(method=HttpMethod.GET)
   // @GetMapping("/{userId}/students")
    public List<StudentModel> getAllStudents() throws ControllerException {
        try {
            return studentService.getAllStudents();
        } catch (ServiceException e) {
            throw new ControllerException("Getting all students fails", e);
        }
    }
    
   // @GetMapping("/{userId}")
    public StudentModel getStudentById(@PathVariable int id) throws ControllerException {
        try {
            return studentService.getStudentById(id);
        } catch (ServiceException e) {
            throw new ControllerException("Getting student by its id failed.", e);
        }
    }
}
