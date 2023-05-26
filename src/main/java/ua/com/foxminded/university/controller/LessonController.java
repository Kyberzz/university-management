package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.TimetableController.TIMETABLE_ID_PARAMETER_NAME;

import java.time.LocalDate;
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
import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TimetableService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController extends DefaultController {
    
    public static final String TIMETABLE_ATTRIBUTE = "timetable";
    public static final String TIMINGS_ATTRIBUTE = "timings";
    public static final String LESSONS_PATH = "/lessons/";
    public static final String MONTH_SHEDULE_TEMPLATE = "month-lessons";
    public static final String MONTH_SHEDULE_TEMPLATE_PATH = "lessons/month-lessons";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String DAY_LESSONS_PATH = "/lessons/day-lessons/";
    public static final String DAY_LESSONS_TEMPLATE = "lessons/day-lessons";
    public static final String LESSON_ATTRIBUTE = "lesson";
    public static final String DAY_LESSONS_ATTRIBUTE = "dayLessons";
    public static final String MONTH_LESSONS_ATTRIBUTE = "monthLessons";
    public static final String TIMETABLES_ATTRIBUTE = "timetables";
    
    private final LessonService lessonService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final TimetableService timetableService;

    @PostMapping("/{date}/create")
    public String create(@PathVariable String date,
                         @RequestParam int timetableId,
                         @ModelAttribute LessonDTO lesson, 
                         Model model) throws ServiceException {
        lesson.setDatestamp(LocalDate.parse(date));
        lesson.setTimetable(TimetableDTO.builder().id(timetableId).build());
        lessonService.create(lesson);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_LESSONS_PATH)
                                  .append(lesson.getDatestamp())
                                  .append("?")
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(lesson.getTimetable().getId())
                                  .toString();
    }
    
    @PostMapping("/delete/{lessonId}")
    public String delete(@ModelAttribute LessonDTO lessonModel, 
                         @PathVariable int lessonId) throws ServiceException {
        lessonService.deleteById(lessonId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_LESSONS_PATH)
                                  .append(lessonModel.getDatestamp())
                                  .append("?")
                                  .toString();
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
    
    @GetMapping("/day-lessons/{date}")
    public String getDayLessons(@PathVariable String date,
                                @RequestParam int timetableId,
                                Model model) throws ServiceException {
        LocalDate datestamp = LocalDate.parse(date);
        List<LessonDTO> dayLessons = lessonService.getDayLessons(datestamp);
        lessonService.addLessonTiming(dayLessons);
        lessonService.sortByLessonOrder(dayLessons);
        LessonDTO lessonModel = LessonDTO.builder().datestamp(datestamp)
                                                   .course(new CourseDTO())
                                                   .build();
        List<CourseDTO> courses = courseService.getAll();
        List<GroupDTO> groups = groupService.getAll();
        List<TimetableDTO> timetables = timetableService.getAll();
        TimetableDTO timetable = timetableService.getByIdWithTimings(timetableId);
        timetableService.sortTimingsByStartTime(timetable);
        
        model.addAttribute(TIMETABLE_ATTRIBUTE, timetable);
        model.addAttribute(TIMETABLES_ATTRIBUTE, timetables);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(DAY_LESSONS_ATTRIBUTE, dayLessons);
        model.addAttribute(LESSON_ATTRIBUTE, lessonModel);
        return DAY_LESSONS_TEMPLATE;
    }
    
    @GetMapping("/{date}/back")
    public String back(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = lessonService.moveBack(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(LESSONS_PATH)
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
                                  .append(LESSONS_PATH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(MONTH_SHEDULE_TEMPLATE)
                                  .append("?").toString();
    }
    
    @GetMapping("/month-lessons/{date}")
    public String getMonthLessons(@PathVariable String date,
                                  Model model) throws ServiceException {
        LocalDate datestamp = LocalDate.parse(date);
        List<List<List<LessonDTO>>> monthLessons = lessonService.getMonthLessons(datestamp);
        LessonDTO lesson = LessonDTO.builder()
                                    .datestamp(datestamp).build();
        lesson.setDatestamp(datestamp);
        List<CourseDTO> courses = courseService.getAll();
        List<GroupDTO> groups = groupService.getAll();
        
        model.addAttribute(LESSON_ATTRIBUTE, lesson);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(MONTH_LESSONS_ATTRIBUTE, monthLessons);
        return MONTH_SHEDULE_TEMPLATE_PATH;
    }
}
