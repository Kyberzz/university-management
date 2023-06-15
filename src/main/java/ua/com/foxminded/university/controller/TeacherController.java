package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.UserController.USERS_ATTRIBUTE;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.comparator.LessonDTOComparator;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
@Validated
public class TeacherController extends DefaultController {
    
    public static final String TEACHER_TEMPLATE_PATH = "teachers/teacher";
    public static final int TEACHER_ID = 1;
    public static final String TEACHERS_LIST_TEMLPATE_PATH = "teachers/list";
    public static final String TEACHERS_ATTRIBUTE = "teachers";
    public static final String TEACHER_ATTRIBUTE = "teacher";

    private final TeacherService teacherService;
    private final UserService userService;
    private final LessonService lessonService;
    
    @PostMapping("/{teacherId}/delete-lesson")
    public String deleteTeacherLesson(@PathVariable int teacherId, 
                                      @RequestParam int lessonId) {
        lessonService.deleteById(lessonId);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHER_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(teacherId)
                                  .toString();
    }
    
    @GetMapping("/teacher/{teacherId}")
    public String getById(@PathVariable int teacherId, Model model) {
        TeacherDTO teacher = teacherService.getById(teacherId);
        List<LessonDTO> lessons = lessonService.getLessonsByTeacherId(teacherId);
        lessonService.addLessonTiming(lessons);
        lessons.sort(new LessonDTOComparator());
        List<UserDTO> users = userService.getAll();
        
        model.addAttribute(USERS_ATTRIBUTE, users);
        model.addAttribute(LessonController.LESSONS_ATTRIBUTE, lessons);
        model.addAttribute(TEACHER_ATTRIBUTE, teacher);
        return TEACHER_TEMPLATE_PATH;
    }
    
    @PostMapping("/{teacherId}/update")
    public String update(@PathVariable int teacherId, 
                         @RequestParam int userId) {
        
        TeacherDTO persistedTeacher = teacherService.getById(teacherId);
        persistedTeacher.setUser(new UserDTO());
        persistedTeacher.getUser().setId(userId);
        teacherService.update(persistedTeacher);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHER_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(teacherId)                                  .toString();
    }
    
    @PostMapping("/delete/{teacherId}")
    public String deleteById(@PathVariable int teacherId) {
        teacherService.deleteById(teacherId);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHERS_LIST_TEMLPATE_PATH)
                                  .toString();
    }
    
    @PostMapping("/create")
    public String create(@RequestParam int userId) {
        TeacherDTO teacher = TeacherDTO.builder()
                                       .user(new UserDTO()).build();
        teacher.getUser().setId(userId);
        teacherService.create(teacher);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHERS_LIST_TEMLPATE_PATH)
                                  .toString();
    }

    @RequestMapping("/list")
    public String getAllTeachers(Model model) {
        List<TeacherDTO> teachers = teacherService.getAll();
        teacherService.sortByLastName(teachers);
        TeacherDTO teacher = TeacherDTO.builder().build();
        List<UserDTO> users = userService.getAll();
        
        model.addAttribute(USERS_ATTRIBUTE, users);
        model.addAttribute(TEACHER_ATTRIBUTE, teacher);
        model.addAttribute(TEACHERS_ATTRIBUTE, teachers);
        return TEACHERS_LIST_TEMLPATE_PATH;
    }
}
