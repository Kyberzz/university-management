package ua.com.foxminded.university.controller;

import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@Slf4j
@Controller
@RequestMapping("/students")
public class StudentController extends DefaultController {
    
    public static final String DEFAULT_PASSWORD = "{noop}password";

    private StudentService<StudentModel> studentService;
    private GroupService<GroupModel> groupService;

    public StudentController(StudentService<StudentModel> studentService, 
                             GroupService<GroupModel> groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @PostMapping("/add")
    public String addStudent(StudentModel studentModel, 
                             BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            handleBindingResultError(bindingResult);
        }

        studentService.addStudent(studentModel);
        return "redirect:/students/list";
    }

    @RequestMapping("/list")
    public String getAllStudents(Model model) throws ServiceException {
        List<StudentModel> students = studentService.getAllStudentsIncludingEmails();
        List<GroupModel> groups = groupService.getAllGroups();
        StudentModel student = new StudentModel();
        model.addAttribute("studentModel", student);
        model.addAttribute("students", students);
        model.addAttribute("groups", groups);
        return "students/list";
    }
    
    @PostMapping(value = "/edit", params = "studentId")
    public String editStudent(@RequestParam("studentId") int studentId, 
                              StudentModel studentModel, 
                              BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            handleBindingResultError(bindingResult);
        }
        StudentModel persistedStudent = studentService.getStudentById(studentId);
        persistedStudent.setFirstName(studentModel.getFirstName());
        persistedStudent.setLastName(studentModel.getLastName());
        
        if (studentModel.hasUser()) {
            String email = studentModel.getUser().getEmail();
            
            if (persistedStudent.hasUser()) {
                persistedStudent.getUser().setEmail(email);
            } else {
                studentModel.getUser().setEnabled(false);
                studentModel.getUser().setPassword(DEFAULT_PASSWORD);
                persistedStudent.setUser(studentModel.getUser());
            }
        }

        if (studentModel.hasGroup()) {
            GroupModel group = studentModel.getGroup();
            group.setStudent(persistedStudent);
            persistedStudent.setGroup(group);
        } else {
            persistedStudent.setGroup(null);
        }
        studentService.updateStudent(persistedStudent);
        return "redirect:/students/list";
    }
    
    @PostMapping(value ="/delete", params = "deleteStudentId")
    public String deleteStudent(@RequestParam("deleteStudentId") int studentId)
            throws ServiceException {
        studentService.deleteStudentById(studentId);
        return "redirect:/students/list";
    }
    
    private String handleBindingResultError(BindingResult bindingResult) {
        bindingResult.getAllErrors()
                     .stream()
                     .forEach(error -> log.error(error.getDefaultMessage()));
        return "error";
    }
}
