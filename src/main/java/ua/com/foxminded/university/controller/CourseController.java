package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.TeacherController.TEACHERS_ATTRIBUTE;

import java.util.List;
import java.util.Set;

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
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController extends DefaultController {
    
    public static final String COURSES_LIST_TEMPLATE_PATH = "courses/list";
    public static final String COURSE_ID_PARAMETER_NAME = "courseId";
    public static final String UPDATED_COURSE_ATTRIBUTE = "updatedCourse";
    public static final String COURSE_TEMPLATE_PATH = "courses/course";
    public static final String COURSE_TEMPLATE = "course";
    public static final String COURSE_ATTRIBUTE = "course";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String COURSES_PATH = "/courses/";
    
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final LessonService lessonService;
    
    @GetMapping("/list/{teacherEmail}")
    public String getByTeacherEmail(@PathVariable String teacherEmail, Model model) {
        TeacherDTO teacher = teacherService.getTeacherByEmail(teacherEmail);
        List<CourseDTO> courses = courseService.getByTeacherId(teacher.getId());
        CourseDTO course = new CourseDTO();
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(COURSE_ATTRIBUTE, course);
        return COURSES_LIST_TEMPLATE_PATH;
    }
    
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
    
    @GetMapping("/{couseId}")
    public String getById(@PathVariable("couseId") int courseId, Model model) {
        CourseDTO course = courseService.getByIdWithLessonsAndTeachers(courseId);
        
        Set<TeacherDTO> courseTeachers = course.getTeachers();
        teacherService.sortByLastName(courseTeachers);
        course.setTeachers(courseTeachers);
        
        Set<LessonDTO> courseLessons = lessonService.sortByDatestamp(course.getLessons());
        course.setLessons(courseLessons);
        
        CourseDTO updatedCourse = new CourseDTO();
        List<TeacherDTO> teachers = teacherService.getAll();
        teacherService.sortByLastName(teachers);
        
        model.addAttribute(TEACHERS_ATTRIBUTE, teachers);
        model.addAttribute(UPDATED_COURSE_ATTRIBUTE, updatedCourse);
        model.addAttribute(COURSE_ATTRIBUTE, course);
        return COURSE_TEMPLATE_PATH;
    }
    
    @PostMapping(value = "/delete", params = "courseId")
    public String deleteById(@RequestParam Integer courseId) {
        courseService.deleteById(courseId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(COURSES_LIST_TEMPLATE_PATH)
                                  .toString();
    }
    
    @PostMapping(value = "/update", params = "courseId")
    public String update(@RequestParam Integer courseId,
                         @Valid @ModelAttribute(UPDATED_COURSE_ATTRIBUTE) CourseDTO updatedCourse) {
        updatedCourse.setId(courseId);
        courseService.update(updatedCourse);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(COURSES_PATH)
                                  .append(courseId).toString();
    }
    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute(COURSE_ATTRIBUTE) CourseDTO course) {
        courseService.create(course);   
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(COURSES_LIST_TEMPLATE_PATH)
                                  .toString();
    }
    
    @GetMapping("/list")
    public String getAll(Model model) {
        CourseDTO course = new CourseDTO();
        List<CourseDTO> courses = courseService.getAll();
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(COURSE_ATTRIBUTE, course);
        return COURSES_LIST_TEMPLATE_PATH;
    }
}
