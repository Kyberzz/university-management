package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.CourseController.COURSE_ID_PARAMETER_NAME;
import static ua.com.foxminded.university.controller.TimetableController.TIMETABLE_ID_PARAMETER_NAME;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
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
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.TimetableService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
@Validated
public class LessonController extends DefaultController {
    
    public static final String COURSE_ATTRIBUTE = "course";
    public static final String TEACHERS_ATTRIBUTE = "teachers";
    public static final String TEACHER_ATTRIBUTE = "teacher";
    public static final String TEACHER_WEEK_SCHEDULE_TEMPLATE_PATH = "lessons/teacher-week-schedule";
    public static final String WEEK_LESSONS_ATTRIBUTE = "weekLessons";
    public static final String TIMETABLE_ATTRIBUTE = "timetable";
    public static final String TIMINGS_ATTRIBUTE = "timings";
    public static final String LESSONS_PATH = "/lessons/";
    public static final String MONTH_SHEDULE_TEMPLATE = "month-lessons";
    public static final String MONTH_SHEDULE_TEMPLATE_PATH = "lessons/month-lessons";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String DAY_LESSONS_TEMPLATE_PATH = "lessons/day-lessons";
    public static final String LESSON_ATTRIBUTE = "lesson";
    public static final String DAY_LESSONS_ATTRIBUTE = "dayLessons";
    public static final String MONTH_LESSONS_ATTRIBUTE = "monthLessons";
    public static final String TIMETABLES_ATTRIBUTE = "timetables";
    
    private final LessonService lessonService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final TimetableService timetableService;
    private final TeacherService teacherService;
    
    @GetMapping("/teacher-week-schedule/{email}")
    public String getScheduleForDate(@PathVariable String email, 
                                     @RequestParam @NotBlank String date) {

        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHER_WEEK_SCHEDULE_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(LocalDate.parse(date))
                                  .append(SLASH)
                                  .append(email)
                                  .toString();
    }
    
    @GetMapping("/teacher-week-schedule/{date}/{email}/back")
    public String getPreviousWeekSchedule(@PathVariable String date, 
                                          @PathVariable String email) {
        
        LocalDate datestamp = lessonService.moveWeekBack(LocalDate.parse(date));
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHER_WEEK_SCHEDULE_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(email)
                                  .toString();
    }
    
    @GetMapping("/teacher-week-schedule/{date}/{email}/next")
    public String getNextWeekSchedule(@PathVariable String date, 
                                      @PathVariable String email) {
        
        LocalDate datestamp = lessonService.moveWeekForward(LocalDate.parse(date));
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(TEACHER_WEEK_SCHEDULE_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(email)
                                  .toString();
    }
    
    @GetMapping("/teacher-week-schedule/{date}/{email}")
    public String getTeacherWeekSchedule(@PathVariable String date, 
                                         @PathVariable String email, Model model) {
       
       TeacherDTO teacher = teacherService.getTeacherByEmail(email);
       List<List<LessonDTO>> weekLessons = lessonService.getWeekLessonsOwnedByTeacher(
               LocalDate.parse(date), teacher.getId());
       LessonDTO lesson = LessonDTO.builder().datestamp(LocalDate.parse(date)).build();
       
       model.addAttribute(WEEK_LESSONS_ATTRIBUTE, weekLessons);
       model.addAttribute(LESSON_ATTRIBUTE, lesson);
       return TEACHER_WEEK_SCHEDULE_TEMPLATE_PATH;
    }
    
    @PostMapping("/{date}/apply-timetable")
    public String applyTimetable(@PathVariable String date, 
                                 @RequestParam int timetableId) {
        lessonService.applyTimetable(LocalDate.parse(date), timetableId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(DAY_LESSONS_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(date)
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(timetableId).toString();
    }
    
    @PostMapping("/{date}/create")
    public String create(@PathVariable String date,
                         @RequestParam @Min(1) int timetableId,
                         @RequestParam @Min(1) int courseId,
                         @RequestParam @Min(1) int groupId,
                         @ModelAttribute(LESSON_ATTRIBUTE) LessonDTO lesson) {
        lesson.setDatestamp(LocalDate.parse(date));
        lesson.setTimetable(TimetableDTO.builder().id(timetableId).build());
        lesson.setCourse(CourseDTO.builder().id(courseId).build());
        lesson.setGroups(new HashSet<>());
        lesson.getGroups().add(GroupDTO.builder().id(groupId).build());
        lessonService.create(lesson);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(DAY_LESSONS_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(lesson.getDatestamp())
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(lesson.getTimetable().getId())
                                  .append("&")
                                  .append(COURSE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(courseId).toString();
    }
    
    @PostMapping("/delete/{lessonId}")
    public String deleteById(@ModelAttribute(LESSON_ATTRIBUTE) LessonDTO lesson,
                             @PathVariable int lessonId) {
        lessonService.deleteById(lessonId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SLASH)
                                  .append(DAY_LESSONS_TEMPLATE_PATH)
                                  .append(SLASH)
                                  .append(lesson.getDatestamp())
                                  .append(QUESTION_MARK)
                                  .append(TIMETABLE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(lesson.getTimetable().getId())
                                  .append("&")
                                  .append(COURSE_ID_PARAMETER_NAME)
                                  .append(EQUAL_SIGN)
                                  .append(STUB).toString();
    }
    
    @GetMapping("/day-lessons/{date}")
    public String getDayLessons(@PathVariable String date,
                                @RequestParam int timetableId,
                                @RequestParam int courseId,
                                Model model) {
        LocalDate datestamp = LocalDate.parse(date);
        List<LessonDTO> dayLessons = lessonService.getDayLessons(datestamp);
        lessonService.addLessonTiming(dayLessons);
        lessonService.sortByLessonOrder(dayLessons);
        LessonDTO lesson = LessonDTO.builder().datestamp(datestamp)
                                              .timetable(TimetableDTO.builder()
                                                                     .id(timetableId)
                                                                     .build())
                                              .build();
        
        List<CourseDTO> courses = courseService.getAll();
        List<GroupDTO> groups = groupService.getAll();
        List<TimetableDTO> timetables = timetableService.getAll();
        
        TimetableDTO timetable = TimetableDTO.builder().id(STUB).build();
        
        if (timetableId != STUB) {
            timetable = timetableService.getByIdWithTimings(timetableId);
        }
        
        List<TeacherDTO> teachers = new ArrayList<>();
        CourseDTO course = CourseDTO.builder().id(STUB).build();
        
        if (courseId != STUB) {
            teachers = teacherService.getByCoursesId(courseId);
            course = courseService.getById(courseId);
        } 
        
        model.addAttribute(COURSE_ATTRIBUTE, course);
        model.addAttribute(TEACHERS_ATTRIBUTE, teachers);
        model.addAttribute(TIMETABLE_ATTRIBUTE, timetable);
        model.addAttribute(TIMETABLES_ATTRIBUTE, timetables);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(DAY_LESSONS_ATTRIBUTE, dayLessons);
        model.addAttribute(LESSON_ATTRIBUTE, lesson);
        return DAY_LESSONS_TEMPLATE_PATH;
    }
    
    @GetMapping("/{date}/back")
    public String back(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = lessonService.moveMonthBack(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(LESSONS_PATH)
                                  .append(MONTH_SHEDULE_TEMPLATE)
                                  .append(SLASH)
                                  .append(datestamp)
                                  .append("?").toString();
    }
    
    @GetMapping("/{date}/next")
    public String next(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = lessonService.moveMonthForward(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(LESSONS_PATH)
                                  .append(MONTH_SHEDULE_TEMPLATE)
                                  .append(SLASH)
                                  .append(datestamp)
                                  .append("?").toString();
    }
    
    @GetMapping("/month-lessons/{date}")
    public String getMonthLessons(@PathVariable String date,
                                  Model model) {
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
