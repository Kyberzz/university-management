package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.GroupController.GROUPS_ATTRIBUTE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.UserService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController extends DefaultController {
    
    public static final String STUDENT_ID_PARAMETER =  "studentId";
    public static final String STUDENTS_ATTRIBUTE = "students";
    public static final String STUDENT_ATTRIBUTE = "student";
    public static final String STUDENTS_LIST_TEMPLATE_PATH = "students/list";
    
    private final StudentService studentService;
    private final GroupService groupService;
    private final UserService userService;
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(STUDENT_ATTRIBUTE) StudentDTO student) {
        UserDTO createdUser = userService.createUserPerson(student.getUser());
        student.setUser(createdUser);
        studentService.create(student);
        
        if (student.getUser().hasEmail()) {
            userService.updateEmail(createdUser.getId(), student.getUser().getEmail());
        }
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(STUDENTS_LIST_TEMPLATE_PATH)
                                  .toString();
    }

    @RequestMapping("/list")
    public String getAll(Model model) {
        List<StudentDTO> students = studentService.getAll();
        List<GroupDTO> groups = groupService.getAll();
        StudentDTO student = new StudentDTO();
        model.addAttribute(STUDENT_ATTRIBUTE, student);
        model.addAttribute(STUDENTS_ATTRIBUTE, students);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        return STUDENTS_LIST_TEMPLATE_PATH;
    }
    
    @PostMapping("/{studentId}/update")
    public String update(@PathVariable int studentId, 
                         @Valid @ModelAttribute(STUDENT_ATTRIBUTE) StudentDTO student) {
        StudentDTO persistedStudent = studentService.getById(studentId);
        
        if (student.hasGroup()) {
            persistedStudent.setGroup(student.getGroup());
        } else {
            persistedStudent.setGroup(null);
        }
        
        studentService.update(persistedStudent);
        int userId = persistedStudent.getUser().getId();
        
        if (student.getUser().hasEmail()) {
            userService.updateEmail(userId, student.getUser().getEmail());
            
        }
        student.getUser().setId(userId);
        userService.updateUserPerson(student.getUser());
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(STUDENTS_LIST_TEMPLATE_PATH).toString();
    }
    
    @PostMapping("/delete")
    public String delete(@RequestParam int studentId) {
        studentService.deleteById(studentId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(STUDENTS_LIST_TEMPLATE_PATH).toString();
    }
}
