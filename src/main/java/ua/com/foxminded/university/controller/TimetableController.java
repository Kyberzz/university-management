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
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.TimetableService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timetables")
public class TimetableController extends DefaultController {
    
    public static final String DAY_TIMETABLE_TEMPLATE = "timetables/day-timetable";
    public static final String COURSES_ATTRIBUTE = "courses";
    public static final String GROUPS_ATTRIBUTE = "groups";
    public static final String DAY_TIMETABLES_PATH = "/timetables/daytimetables/";
    public static final String TIMETABLES_LIST_TEMPLATE = "timetables/list";
    public static final String TIMETABLE_MODEL_ATTRIBUTE = "timetableModel";
    public static final String DAY_TIMETABLE_ATTRIBUTE = "dayTimetable";
    public static final String MONTH_TIMETABLE_ATTRIBUTE = "monthTimetable";
    
    private final TimetableService timetableService;
    private final CourseService courseService;
    private final GroupService groupService;

    @PostMapping("/create/timetable/{date}")
    public String create(@PathVariable String date, 
                         @ModelAttribute TimetableModel timetableModel, 
                         Model model) throws ServiceException {
        timetableModel.setDatestamp(LocalDate.parse(date));
        timetableService.create(timetableModel);
        
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_TIMETABLES_PATH)
                                  .append(timetableModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/delete/{id}")
    public String delete(@ModelAttribute TimetableModel timetableModel, 
                         @PathVariable int id) throws ServiceException {
        timetableService.deleteById(id);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_TIMETABLES_PATH)
                                  .append(timetableModel.getDatestamp())
                                  .append("?").toString();
    }
    
    @PostMapping("/update/{id}")
    public String update(@PathVariable int id, 
                         @ModelAttribute TimetableModel timetableModel, 
                         Model model) throws ServiceException {
        
        timetableService.update(timetableModel);
        return new StringBuilder().append(REDIRECT_KEY_WORD)
                                  .append(DAY_TIMETABLES_PATH)
                                  .append(timetableModel.getDatestamp())
                                  .append("?")
                                  .toString();
    }
    
    @GetMapping("/month-timetables/{datestamp}")
    public String getMonthTimetable(@PathVariable String datestamp, 
                                    Model model) throws ServiceException{
        LocalDate date = LocalDate.parse(datestamp);
        List<List<List<TimetableModel>>> monthTimetables = timetableService
                .getMonthTimetable(date);
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setDatestamp(date);
        model.addAttribute(MONTH_TIMETABLE_ATTRIBUTE, monthTimetables);
        model.addAttribute(TIMETABLE_MODEL_ATTRIBUTE, timetableModel);
        return TIMETABLES_LIST_TEMPLATE;
    }
    
    @GetMapping("/day-timetables/{datestamp}")
    public String getDayTimetable(@PathVariable String datestamp,
                                  Model model) throws ServiceException {
        LocalDate localDate = LocalDate.parse(datestamp);
        List<TimetableModel> dayTimetable = timetableService.getDayTimetalbe(localDate);
        TimetableModel timetableModel = new TimetableModel();
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
    public String back(@PathVariable String date, 
                       Model model) throws ServiceException {
        
        timetableService.moveBackDatestamp(timetableModel);
        List<List<List<TimetableModel>>> monthTimetable = timetableService
                .getMonthTimetable(timetableModel.getDatestamp());
        model.addAttribute(MONTH_TIMETABLE_ATTRIBUTE, monthTimetable);
        model.addAttribute(timetableModel);
        return TIMETABLES_LIST_TEMPLATE;
    }
    
    @PostMapping(value = "/next")
    public String next(@ModelAttribute TimetableModel timetableModel, 
                       Model model) throws ServiceException {
        timetableService.moveForwardDatestamp(timetableModel);
        List<List<List<TimetableModel>>> monthTimtable = timetableService
                .getMonthTimetable(timetableModel.getDatestamp());
        model.addAttribute(MONTH_TIMETABLE_ATTRIBUTE, monthTimtable);
        model.addAttribute(timetableModel);
        return TIMETABLES_LIST_TEMPLATE;
    }
    
    @GetMapping(value = "/list")
    public String list(Model model) throws ServiceException {
        LocalDate currentDate = LocalDate.now();
        List<List<List<TimetableModel>>> monthTimetable = timetableService
                .getMonthTimetable(currentDate);
        TimetableModel timetableModel = new TimetableModel();
        timetableModel.setDatestamp(currentDate);
        List<CourseModel> courses = courseService.getAll();
        List<GroupModel> groups = groupService.getAll();
        
        model.addAttribute(GROUPS_ATTRIBUTE, groups);
        model.addAttribute(COURSES_ATTRIBUTE, courses);
        model.addAttribute(MONTH_TIMETABLE_ATTRIBUTE, monthTimetable);
        model.addAttribute(TIMETABLE_MODEL_ATTRIBUTE, timetableModel);
        return TIMETABLES_LIST_TEMPLATE;
    }
}
