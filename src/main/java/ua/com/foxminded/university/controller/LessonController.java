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
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TimetableService;
import ua.com.foxminded.university.service.TimingService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController extends DefaultController {
    
    public static final String TIMINGS_ATTRIBUTE = "timings";
    public static final String SHEDULES_PATH = "/lessons/";
    public static final String MONTH_SHEDULE_TEMPLATE = "month-lessons";
    public static final String MONTH_SHEDULE_TEMPLATE_PATH = "lessons/month-lessons";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String DAY_LESSONS_PATH = "/lessons/day-lessons/";
    public static final String DAY_LESSONS_TEMPLATE = "lessons/day-lessons";
    public static final String LESSON_MODEL_ATTRIBUTE = "lessonModel";
    public static final String DAY_LESSONS_ATTRIBUTE = "dayLessons";
    public static final String MONTH_LESSONS_ATTRIBUTE = "monthLessons";
    public static final String TIMETABLES_ATTRIBUTE = "timetables";
    
    private final LessonService lessonService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final TimetableService timetableService;
    private final TimingService timingService;

    @PostMapping("/{date}/create")
    public String create(@PathVariable String date, 
                         @ModelAttribute LessonDTO lessonModel, 
                         Model model) throws ServiceException {
        lessonModel.setDatestamp(LocalDate.parse(date));
        lessonService.create(lessonModel);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_LESSONS_PATH)
                                  .append(lessonModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/delete/{lessonId}")
    public String delete(@ModelAttribute LessonDTO lessonModel, 
                         @PathVariable int lessonId) throws ServiceException {
        lessonService.deleteById(lessonId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_LESSONS_PATH)
                                  .append(lessonModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/update/{lessonId}")
    public String update(@PathVariable int lessonId, 
                         @ModelAttribute LessonDTO lessonModel, 
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
                                @RequestParam int timetableId,
                                Model model) throws ServiceException {
        LocalDate datestamp = LocalDate.parse(date);
        List<LessonDTO> dayLessons = lessonService.getDayLessons(datestamp);
        lessonService.addLessonTiming(dayLessons);
        LessonDTO lessonModel = LessonDTO.builder().datestamp(datestamp)
                                                       .course(new CourseDTO())
                                                       .build();
        List<CourseDTO> courses = courseService.getAll();
        List<GroupDTO> groups = groupService.getAll();
        List<TimetableDTO> timetables = timetableService.getAll();
        List<TimingDTO> timings = timingService.getByTimetableId(timetableId);  
        
        model.addAttribute(TIMINGS_ATTRIBUTE, timings);
        model.addAttribute(TIMETABLES_ATTRIBUTE, timetables);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(DAY_LESSONS_ATTRIBUTE, dayLessons);
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
        List<List<List<LessonDTO>>> monthLessons = lessonService.getMonthLessons(datestamp);
        LessonDTO lessonModel = LessonDTO.builder()
                                             .datestamp(datestamp).build();
        lessonModel.setDatestamp(datestamp);
        List<CourseDTO> courses = courseService.getAll();
        List<GroupDTO> groups = groupService.getAll();
        
        model.addAttribute(LESSON_MODEL_ATTRIBUTE, lessonModel);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(MONTH_LESSONS_ATTRIBUTE, monthLessons);
        return MONTH_SHEDULE_TEMPLATE_PATH;
    }
}
