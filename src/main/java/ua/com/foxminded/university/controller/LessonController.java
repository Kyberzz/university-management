package ua.com.foxminded.university.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.model.LessonModel;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController extends DefaultController {
    
    public static final String SHEDULES_PATH = "/lessons/";
    public static final String MONTH_SHEDULE_TEMPLATE = "month-lessons";
    public static final String MONTH_SHEDULE_TEMPLATE_PATH = "lessons/month-lessons";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String DAY_LESSONS_PATH = "/lessons/day-lessons/";
    public static final String DAY_LESSONS_TEMPLATE = "lessons/day-lesson";
    public static final String LESSON_MODEL_ATTRIBUTE = "lessonModel";
    public static final String DAY_LESSONS_ATTRIBUTE = "dayLesson";
    public static final String MONTH_LESSONS_ATTRIBUTE = "monthLessons";
    
    private final LessonService lessonService;
    private final CourseService courseService;
    private final GroupService groupService;

    @PostMapping("/create/{date}/lesson")
    public String create(@PathVariable String date, 
                         @ModelAttribute LessonModel lessonModel, 
                         Model model) throws ServiceException {
        lessonModel.setDatestamp(LocalDate.parse(date));
        lessonService.create(lessonModel);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_LESSONS_PATH)
                                  .append(lessonModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/delete/{lessonId}")
    public String delete(@ModelAttribute LessonModel lessonModel, 
                         @PathVariable int lessonId) throws ServiceException {
        lessonService.deleteById(lessonId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_LESSONS_PATH)
                                  .append(lessonModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/update/{lessonId}")
    public String update(@PathVariable int lessonId, 
                         @ModelAttribute LessonModel lessonModel, 
                         Model model) throws ServiceException {
        lessonModel.setId(lessonId);
        lessonService.update(lessonModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_LESSONS_PATH)
                                  .append(lessonModel.getDatestamp())
                                  .append("?")
                                  .toString();
    }
    
    @GetMapping("/{date}/day-lessons")
    public String getDayLessons(@PathVariable String date,
                                Model model) throws ServiceException {
        LocalDate localDate = LocalDate.parse(date);
        List<LessonModel> daylessons = lessonService.getDayLessons(localDate);
        LessonModel lessonModel = new LessonModel();
        lessonModel.setDatestamp(localDate);
        lessonModel.setCourse(new CourseModel());
        lessonModel.getCourse().setTeachers(new HashSet<>());
        lessonModel.getCourse().getTeachers().add(new TeacherModel());
        List<CourseModel> courses = courseService.getAll();
        List<GroupModel> groups = groupService.getAll();
        
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(DAY_LESSONS_ATTRIBUTE, daylessons);
        model.addAttribute(LESSON_MODEL_ATTRIBUTE, lessonModel);
        return DAY_LESSONS_TEMPLATE;
    }
    
    @GetMapping(value = "/{date}/back")
    public String back(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = lessonService.moveBack(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SHEDULES_PATH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(MONTH_SHEDULE_TEMPLATE)
                                  .append("?").toString();
    }
    
    @GetMapping(value = "/{date}/next")
    public String next(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = lessonService.moveForward(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SHEDULES_PATH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(MONTH_SHEDULE_TEMPLATE)
                                  .append("?").toString();
    }
    
    @GetMapping("/{date}/month-lessons")
    public String getMonthLessons(@PathVariable String date,
                                  Model model) throws ServiceException {
        LocalDate datestamp = LocalDate.parse(date);
        List<List<List<LessonModel>>> monthLessons = lessonService.getMonthLessons(datestamp);
        LessonModel lessonModel = LessonModel.builder()
                                                      .datestamp(datestamp).build();
        lessonModel.setDatestamp(datestamp);
        List<CourseModel> courses = courseService.getAll();
        List<GroupModel> groups = groupService.getAll();
        
        model.addAttribute(LESSON_MODEL_ATTRIBUTE, lessonModel);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(MONTH_LESSONS_ATTRIBUTE, monthLessons);
        return MONTH_SHEDULE_TEMPLATE_PATH;
    }
}
