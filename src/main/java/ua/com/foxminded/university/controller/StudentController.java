package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.GroupController.GROUPS_MODEL_ATTRIBUTE;

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
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController extends DefaultController {
    
    public static final String STUDENTS_MODEL_ATTRIBUTE = "students";
    public static final String STUDENT_MODEL_ATTRIBUTE = "student";
    public static final String STUDENTS_LIST_TEMPLATE_PATH = "students/list";
    
    private final StudentService studentService;
    private final GroupService groupService;
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute StudentDTO student) {
        
        studentService.create(student);
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
        model.addAttribute(STUDENT_MODEL_ATTRIBUTE, student);
        model.addAttribute(STUDENTS_MODEL_ATTRIBUTE, students);
        model.addAttribute(GROUPS_MODEL_ATTRIBUTE, groups);
        return STUDENTS_LIST_TEMPLATE_PATH;
    }
    
    @PostMapping("/{studentId}/update")
    public String update(@PathVariable int studentId, 
                         @Valid @ModelAttribute StudentDTO student) {
        student.setId(studentId);
        studentService.update(student);
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
