package ua.com.foxminded.university.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.buisness.model.StudentModel;
import ua.com.foxminded.university.buisness.model.service.ServiceException;
import ua.com.foxminded.university.buisness.model.service.StudentService;

@Slf4j
public class StudentFormatter implements Formatter<StudentModel>{
    
    @Autowired
    private StudentService<StudentModel> studentService;
    
    public StudentFormatter() {
        super();
    }

    @Override
    public StudentModel parse(String text, Locale locale) throws ParseException {
        Integer studentId = Integer.valueOf(text);
        StudentModel studentModel = null;
       
        try {
            studentModel = studentService.getStudentById(studentId);
        } catch (ServiceException e) {
           log.error("The student was not recieved from the database"); 
        }
        return studentModel;
    }

    @Override
    public String print(StudentModel student, Locale locale) {
        return (student != null ? String.valueOf(student.getId()) : "");
    }
}
