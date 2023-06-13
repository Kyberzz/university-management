package ua.com.foxminded.university.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
@Validated
public class TeacherController extends DefaultController {
    
    public static final int TEACHER_ID = 1;
    public static final String TEACHERS_LIST_TEMPATE_PATH = "teachers/list";
    public static final String TEACHERS_ATTRIBUTE = "teachers";
    public static final String TEACHER_MODEL_ATTRIBUTE = "teacher";

    private final TeacherService teacherService;
    private final UserService userService;
    
    @PostMapping("/update/{teacherId}")
    public String update(@PathVariable int teacherId,
                         @ModelAttribute TeacherDTO teacherDto) {
        
        TeacherDTO teacher = teacherService.getById(teacherId);
        
        if (teacherDto.getUser().hasEmail()) {
            userService.updateEmail(teacher.getUser().getId(), 
                                    teacherDto.getUser().getEmail());
        }
        
        teacherDto.getUser().setId(teacher.getUser().getId());
        userService.updateUserPerson(teacherDto.getUser());
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHERS_LIST_TEMPATE_PATH)
                                  .toString();
    }
    
    @PostMapping("/delete/{teacherId}")
    public String deleteById(@PathVariable int teacherId) {
        teacherService.deleteById(teacherId);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHERS_LIST_TEMPATE_PATH)
                                  .toString();
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Valid TeacherDTO teacher) {
        
        UserDTO user = userService.createUserPerson(teacher.getUser());
        
        if (teacher.getUser().hasEmail()) {
            user = userService.updateEmail(user.getId(), 
                                           teacher.getUser().getEmail());
        } 
        
        teacher.setUser(user);
        teacherService.create(teacher);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHERS_LIST_TEMPATE_PATH)
                                  .toString();
    }

    @RequestMapping("/list")
    public String getAllTeachers(Model model) {
        List<TeacherDTO> teachers = teacherService.getAll();
        teacherService.sortByLastName(teachers);
        TeacherDTO teacher = TeacherDTO.builder().build();
        model.addAttribute(TEACHER_MODEL_ATTRIBUTE, teacher);
        model.addAttribute(TEACHERS_ATTRIBUTE, teachers);
        return TEACHERS_LIST_TEMPATE_PATH;
    }
}