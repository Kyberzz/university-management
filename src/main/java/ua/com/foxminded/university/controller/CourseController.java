package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.service.CourseService;

@Controller
@RequiredArgsConstructor
public class CourseController extends DefaultController {

    private final CourseService<CourseModel> courseService;

    @RequestMapping("courses/list")
    public String getAllCourses(Model model) throws ServiceException {
        List<CourseModel> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses/list";
    }
}
