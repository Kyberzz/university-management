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
import ua.com.foxminded.university.model.ScheduleModel;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.ScheduleService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController extends DefaultController {
    
    public static final String SHEDULES_PATH = "/schedules/";
    public static final String MONTH_SHEDULE_TEMPLATE = "month-schedule";
    public static final String MONTH_SHEDULE_TEMPLATE_PATH = "schedules/month-schedule";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String DAY_SCHEDULE_PATH = "/schedules/day-schedules/";
    public static final String DAY_SCHEDULE_TEMPLATE = "schedules/day-schedule";
    public static final String SHCEDULE_MODEL_ATTRIBUTE = "scheduleModel";
    public static final String DAY_SCHEDULE_ATTRIBUTE = "daySchedule";
    public static final String MONTH_SCHEDULE_ATTRIBUTE = "monthSchedule";
    
    private final ScheduleService scheduleService;
    private final CourseService courseService;
    private final GroupService groupService;

    @PostMapping("/create/schedule/{date}")
    public String create(@PathVariable String date, 
                         @ModelAttribute ScheduleModel scheduleModel, 
                         Model model) throws ServiceException {
        scheduleModel.setDatestamp(LocalDate.parse(date));
        scheduleService.create(scheduleModel);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_SCHEDULE_PATH)
                                  .append(scheduleModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/delete/{scheduleId}")
    public String delete(@ModelAttribute ScheduleModel scheduleModel, 
                         @PathVariable int scheduleId) throws ServiceException {
        scheduleService.deleteById(scheduleId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_SCHEDULE_PATH)
                                  .append(scheduleModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/update/{id}")
    public String update(@PathVariable int id, 
                         @ModelAttribute ScheduleModel scheduleModel, 
                         Model model) throws ServiceException {
        scheduleModel.setId(id);
        scheduleService.update(scheduleModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_SCHEDULE_PATH)
                                  .append(scheduleModel.getDatestamp())
                                  .append("?")
                                  .toString();
    }
    
    @GetMapping("/day-schedule/{date}")
    public String getDaySchedule(@PathVariable String date,
                                 Model model) throws ServiceException {
        LocalDate localDate = LocalDate.parse(date);
        List<ScheduleModel> daySchedule = scheduleService.getDaySdhedule(localDate);
        ScheduleModel scheduleModel = new ScheduleModel();
        scheduleModel.setDatestamp(localDate);
        scheduleModel.setCourse(new CourseModel());
        scheduleModel.getCourse().setTeachers(new HashSet<>());
        scheduleModel.getCourse().getTeachers().add(new TeacherModel());
        List<CourseModel> courses = courseService.getAll();
        List<GroupModel> groups = groupService.getAll();
        
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(DAY_SCHEDULE_ATTRIBUTE, daySchedule);
        model.addAttribute(SHCEDULE_MODEL_ATTRIBUTE, scheduleModel);
        return DAY_SCHEDULE_TEMPLATE;
    }
    
    @GetMapping(value = "/{date}/back")
    public String back(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = scheduleService.moveBack(localDate);
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
        LocalDate datestamp = scheduleService.moveForward(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(SHEDULES_PATH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(MONTH_SHEDULE_TEMPLATE)
                                  .append("?").toString();
    }
    
    @GetMapping("/{date}/month-schedule")
    public String getMonthSchedule(@PathVariable String date,
                                   Model model) throws ServiceException {
        LocalDate datestamp = LocalDate.parse(date);
        List<List<List<ScheduleModel>>> monthSchedule = scheduleService
                .getMonthSchedule(datestamp);
        ScheduleModel scheduleModel = ScheduleModel.builder()
                                                      .datestamp(datestamp).build();
        scheduleModel.setDatestamp(datestamp);
        List<CourseModel> courses = courseService.getAll();
        List<GroupModel> groups = groupService.getAll();
        
        model.addAttribute(SHCEDULE_MODEL_ATTRIBUTE, scheduleModel);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(MONTH_SCHEDULE_ATTRIBUTE, monthSchedule);
        return MONTH_SHEDULE_TEMPLATE_PATH;
    }
}
