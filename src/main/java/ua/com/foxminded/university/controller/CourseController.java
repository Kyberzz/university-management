package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.TeacherController.TEACHERS_MODEL_ATTRIBUTE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController extends DefaultController {
    
    
    public static final String COURSE_ID_PARAMETER_NAME = "courseId=";
    public static final String UPDATED_COURSE_ATTRIBUTE = "updatedCourse";
    public static final String COURSE_TEMPLATE = "course";
    public static final String COURSE_MODEL_ATTRIBUTE = "course";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String COURSES_PATH = "/courses/";
    
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final LessonService lessonService;
    
    @PostMapping(value = "/{courseId}/deassign-teacher", params = "teacherId")
    public String deassignTeacherToCourse(@PathVariable int courseId, 
                                          @RequestParam int teacherId) {
        courseService.deassignTeacherToCourse(teacherId, courseId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(COURSES_PATH)
                                  .append(courseId).toString();
    }
    
    @PostMapping(value = "/{courseId}/assign-teacher", params = "teacherId")
    public String assignTeacherToCourse(@PathVariable int courseId, 
                                        @RequestParam int teacherId) {
        courseService.assignTeacherToCourse(teacherId, courseId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(COURSES_PATH)
                                  .append(courseId).toString();
    }
    
    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model) {
        CourseDTO course = courseService.getByIdWithLessonsAndTeachers(id);
        course.setLessons(lessonService.sortByDatestamp(course.getLessons()));
        
        CourseDTO updatedCourse = new CourseDTO();
        List<TeacherDTO> teachers = teacherService.getAll();
        teacherService.sortByLastName(teachers);
        
        model.addAttribute(TEACHERS_MODEL_ATTRIBUTE, teachers);
        model.addAttribute(UPDATED_COURSE_ATTRIBUTE, updatedCourse);
        model.addAttribute(COURSE_MODEL_ATTRIBUTE, course);
        return "courses/course";
    }
    
    @PostMapping(value = "/delete", params = "courseId")
    public String delete(@RequestParam Integer courseId) {
        courseService.deleteById(courseId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(COURSES_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
    
    @PostMapping(value = "/update", params = "courseId")
    public String update(@RequestParam Integer courseId,
                         @Valid @ModelAttribute CourseDTO updatedCourse) {
        updatedCourse.setId(courseId);
        courseService.update(updatedCourse);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(COURSES_PATH)
                                  .append(courseId).toString();
    }
    
    @PostMapping(value = "/create")
    public String create(@Valid @ModelAttribute CourseDTO courseModel) {
        courseService.create(courseModel);   
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(COURSES_PATH)
                                  .append(LIST_TEMPLATE).toString();
    }
    
    @GetMapping("/list")
    public String list(Model model) {
        CourseDTO course = new CourseDTO();
        List<CourseDTO> courses = courseService.getAll();
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(COURSE_MODEL_ATTRIBUTE, course);
        return "courses/list";
    }
}
