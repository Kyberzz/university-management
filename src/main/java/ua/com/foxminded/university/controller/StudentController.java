package ua.com.foxminded.university.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    private StudentService<StudentModel> studentService;
    private GroupService<GroupModel> groupService;

    @Autowired
    public StudentController(StudentService<StudentModel> studentService, 
                             GroupService<GroupModel> groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @PostMapping("/add")
    public String addStudent(StudentModel studentModel, 
                             BindingResult bindintResult) throws ServiceException {
        if (bindintResult.hasErrors()) {
            return "error";
        } else {
            studentService.addStudent(studentModel);
            return "redirect:/students/list";
        }
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
    public String editStudent(@RequestParam("studentId") int studentId, StudentModel studentModel, 
                              BindingResult bindingResult, Model model) throws ServiceException {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream()
                                        .forEach(error -> log.error(error.getDefaultMessage()));
            return "error";
        } else {
            if (studentModel.getGroup() != null && studentModel.getGroup().getId() == null) {
                    studentModel.setGroup(null);
            }
            studentModel.setId(studentId);
            studentService.updateStudent(studentModel);
            return "redirect:/students/list";
        }
    }
    
    @PostMapping(value ="/delete", params = "deleteStudentId")
    public String deleteStudent(@RequestParam("deleteStudentId") int studentId)
            throws ServiceException {
        studentService.deleteStudentById(studentId);
        return "redirect:/students/list";
    }
}
