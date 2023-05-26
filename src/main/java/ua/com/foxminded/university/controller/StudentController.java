package ua.com.foxminded.university.controller;

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
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController extends DefaultController {
    
    public static final String STUDENTS_PATH = "/students/";
    
    private final StudentService studentService;
    private final GroupService groupService;
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute StudentDTO studentModel) 
            throws ServiceException {
        
        studentService.create(studentModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(STUDENTS_PATH)
                                  .append(LIST_TEMPLATE)
                                  .toString();
    }

    @RequestMapping("/list")
    public String list(Model model) throws ServiceException {
        List<StudentDTO> students = studentService.getAll();
        List<GroupDTO> groups = groupService.getAll();
        StudentDTO student = new StudentDTO();
        model.addAttribute("studentModel", student);
        model.addAttribute("students", students);
        model.addAttribute("groups", groups);
        return "students/list";
    }
    
    @PostMapping("/{studentId}/update")
    public String update(@PathVariable int studentId, 
                         @Valid @ModelAttribute StudentDTO studentModel) 
                                 throws ServiceException {
        studentModel.setId(studentId);
        studentService.update(studentModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(STUDENTS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
    
    @PostMapping("/delete")
    public String delete(@RequestParam int studentId) throws ServiceException {
        studentService.deleteById(studentId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(STUDENTS_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
}
