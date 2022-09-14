package ua.com.foxminded.university.entity;

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
public class CourseEntity {
    @NonNull private Integer id;
    private TeacherEntity teacher;
    private String name;
    private String description;
    private List<TimetableEntity> timetableList;
}