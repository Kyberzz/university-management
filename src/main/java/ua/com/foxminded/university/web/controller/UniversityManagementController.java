package ua.com.foxminded.university.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.buisness.model.CourseModel;
import ua.com.foxminded.university.buisness.model.GroupModel;
import ua.com.foxminded.university.buisness.model.StudentModel;
import ua.com.foxminded.university.buisness.model.TeacherModel;
import ua.com.foxminded.university.buisness.model.TimetableModel;
import ua.com.foxminded.university.buisness.model.service.CourseService;
import ua.com.foxminded.university.buisness.model.service.GroupService;
import ua.com.foxminded.university.buisness.model.service.ServiceException;
import ua.com.foxminded.university.buisness.model.service.StudentService;
import ua.com.foxminded.university.buisness.model.service.TeacherService;
import ua.com.foxminded.university.buisness.model.service.TimetableService;

@Slf4j
@Controller
public class UniversityManagementController {
    
    private StudentService<StudentModel> studentService;
    private TeacherService<TeacherModel> teacherService;
    private GroupService<GroupModel> groupService;
    private CourseService<CourseModel> courseService;
    private TimetableService<TimetableModel> timetableService;
    
    @Autowired
    public UniversityManagementController(StudentService<StudentModel> studentService, 
                                          TeacherService<TeacherModel> teacherService,
                                          GroupService<GroupModel> groupService,
                                          CourseService<CourseModel> courseService,
                                          TimetableService<TimetableModel> timetableService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.courseService = courseService;
        this.timetableService = timetableService;
    }
    
    @RequestMapping("/")
    public String renderIndexView() {
        return "index";
    }
    
    @GetMapping(value = "/index", params = "getAllTimetables")
    public String getAllTimetables(Model model) {
        try {
            List<TimetableModel> timetables = timetableService.getAllTimetables();
            model.addAttribute("timetables", timetables);
        } catch (ServiceException e) {
            log.error("Getting all timetables was failed", e);
        }
        return "timetables";
    }
    
    @GetMapping(value = "/index", params = "getAllCourses")
    public String getAllCourses(Model model) {
        try {
            List<CourseModel> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
        } catch (ServiceException e) {
            log.error("Getting all courses was failed", e);
        }
        return "courses";
    }
    
    @GetMapping(value = "/index", params = "getAllGroups")
    public String getAllGroups(Model model) {
        try {
            List<GroupModel> groups = groupService.getAllGroups();
            model.addAttribute("groups", groups);
        } catch (ServiceException e) {
            log.error("Getting all groups was failed", e);
        }
        return "groups"; 
    }
    
    @RequestMapping(value = "/index", params = "getAllStudents")
    public String getStudents(Model model) {
        try {
            List<StudentModel> students = studentService.getAllStudents();
            model.addAttribute("students", students);
        } catch (ServiceException e){
            log.error("Getting all students was failed", e);
        }
        return "students";
    }
    
    @RequestMapping(value = "/index", params = "getAllTeachers")
    public String getAllTeachers(Model model) {
        try {
            List<TeacherModel> teachers = teacherService.getAllTeachers();
            model.addAttribute("teachers", teachers);
        } catch (ServiceException e) {
            log.error("Getting all teachers was failed", e);
        }
        return "teachers";
    }
}
