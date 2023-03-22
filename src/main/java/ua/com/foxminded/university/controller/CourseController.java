package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.service.CourseService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController extends DefaultController {

    private final CourseService<CourseModel> courseService;

    @GetMapping("/list")
    public String getAll(Model model) throws ServiceException {
        List<CourseModel> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses/list";
    }
    
//    @ModelAttribute("course")
//    public CourseModel addCourseAttribute() {
//        
//        
//    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute CourseModel course) {
        
        
        
        
        
        return "courses/list";
    }
}
