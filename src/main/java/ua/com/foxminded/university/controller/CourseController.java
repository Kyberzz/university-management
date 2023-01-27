package ua.com.foxminded.university.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.service.CourseService;

@Slf4j
@Controller
public class CourseController extends DefaultController {

    private CourseService<CourseModel> courseService;

    @Autowired
    public CourseController(CourseService<CourseModel> courseService) {
        this.courseService = courseService;
    }

    @RequestMapping("courses/list")
    public String getAllCourses(Model model) throws ServiceException {
        List<CourseModel> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses/list";
    }
}
