package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.StudentDTO;

public interface StudentService extends GenericService<StudentDTO> {
    
    public StudentDTO getByEmail(String email);
    
    public void sortByLastName(List<StudentDTO> students);
}
