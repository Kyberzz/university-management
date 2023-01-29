package ua.com.foxminded.university.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.StudentService;

@Slf4j
@Controller
public class StudentController extends DefaultController {

    private StudentService<StudentModel> studentService;

    @Autowired
    public StudentController(StudentService<StudentModel> studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("/students/list")
    public String getAllStudents(Model model) throws ServiceException {
        List<StudentModel> students = studentService.getAllStudentsWithEmail();
        StudentModel student = new StudentModel();
        model.addAttribute("editedStudent", student);
        model.addAttribute("students", students);
        return "students/list";
    }
    
    @PostMapping(value = "/students/list", params = "studentId")
    public String editStdent(@RequestParam("studentId") int studentId, StudentModel editedStudent, 
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "error";
        } else {
            
            log.error(editedStudent.toString());
            log.error(String.valueOf(studentId));
            return "redirect:/students/list";
        }
    }
    
    @PostMapping(value ="/students/list", params = "deleteStudentId")
    public String deleteStudent(@RequestParam("deleteStudentId") int studentId) {
        log.error(String.valueOf(studentId));
        return "redirect:/students/list";
    }
}
