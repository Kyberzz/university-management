package ua.com.foxminded.university.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter @Setter @EqualsAndHashCode @ToString
public class CourseModel {
    @NonNull private Integer id;
    private TeacherModel teacher;
    private String name;
    private String description;
    private List<TimetableModel> timetableList;
}