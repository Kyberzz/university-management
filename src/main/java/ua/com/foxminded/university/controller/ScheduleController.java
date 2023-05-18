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
@RequestMapping("/timetables")
public class ScheduleController extends DefaultController {
    
    public static final String TIMETABLES_PATH = "/timetables/";
    public static final String DAY_TIMETABLE_TEMPLATE = "timetables/day-timetable";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String DAY_TIMETABLES_PATH = "/timetables/day-timetables/";
    public static final String TIMETABLES_LIST_TEMPLATE = "timetables/list";
    public static final String TIMETABLE_MODEL_ATTRIBUTE = "timetableModel";
    public static final String DAY_TIMETABLE_ATTRIBUTE = "dayTimetable";
    public static final String MONTH_TIMETABLE_ATTRIBUTE = "monthTimetable";
    
    private final ScheduleService timetableService;
    private final CourseService courseService;
    private final GroupService groupService;

    @PostMapping("/create/timetable/{date}")
    public String create(@PathVariable String date, 
                         @ModelAttribute ScheduleModel timetableModel, 
                         Model model) throws ServiceException {
        timetableModel.setDatestamp(LocalDate.parse(date));
        timetableService.create(timetableModel);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_TIMETABLES_PATH)
                                  .append(timetableModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/delete/{timetableId}")
    public String delete(@ModelAttribute ScheduleModel timetableModel, 
                         @PathVariable int timetableId) throws ServiceException {
        timetableService.deleteById(timetableId);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_TIMETABLES_PATH)
                                  .append(timetableModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/update/{id}")
    public String update(@PathVariable int id, 
                         @ModelAttribute ScheduleModel timetableModel, 
                         Model model) throws ServiceException {
        timetableModel.setId(id);
        timetableService.update(timetableModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_TIMETABLES_PATH)
                                  .append(timetableModel.getDatestamp())
                                  .append("?")
                                  .toString();
    }
    
    @GetMapping("/day-timetables/{date}")
    public String getDayTimetable(@PathVariable String date,
                                  Model model) throws ServiceException {
        LocalDate localDate = LocalDate.parse(date);
        List<ScheduleModel> dayTimetable = timetableService.getDayTimetalbe(localDate);
        ScheduleModel timetableModel = new ScheduleModel();
        timetableModel.setDatestamp(localDate);
        timetableModel.setCourse(new CourseModel());
        timetableModel.getCourse().setTeachers(new HashSet<>());
        timetableModel.getCourse().getTeachers().add(new TeacherModel());
        List<CourseModel> courses = courseService.getAll();
        List<GroupModel> groups = groupService.getAll();
        
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(DAY_TIMETABLE_ATTRIBUTE, dayTimetable);
        model.addAttribute(TIMETABLE_MODEL_ATTRIBUTE, timetableModel);
        return DAY_TIMETABLE_TEMPLATE;
    }
    
    @GetMapping(value = "/{date}/back")
    public String back(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = timetableService.moveBack(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(TIMETABLES_PATH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(LIST_TEMPLATE)
                                  .append("?").toString();
    }
    
    @GetMapping(value = "/{date}/next")
    public String next(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate datestamp = timetableService.moveForward(localDate);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(TIMETABLES_PATH)
                                  .append(datestamp)
                                  .append(SLASH)
                                  .append(LIST_TEMPLATE)
                                  .append("?").toString();
    }
    
    @GetMapping(value = "/{date}/list")
    public String list(@PathVariable String date,
                       Model model) throws ServiceException {
        LocalDate datestamp = LocalDate.parse(date);
        List<List<List<ScheduleModel>>> monthTimetable = timetableService
                .getMonthTimetable(datestamp);
        ScheduleModel timetableModel = ScheduleModel.builder()
                                                      .datestamp(datestamp).build();
        timetableModel.setDatestamp(datestamp);
        List<CourseModel> courses = courseService.getAll();
        List<GroupModel> groups = groupService.getAll();
        
        model.addAttribute(TIMETABLE_MODEL_ATTRIBUTE, timetableModel);
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(MONTH_TIMETABLE_ATTRIBUTE, monthTimetable);
        return TIMETABLES_LIST_TEMPLATE;
    }
}