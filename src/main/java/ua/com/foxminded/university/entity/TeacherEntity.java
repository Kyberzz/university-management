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
public class TeacherEntity {

    @NonNull private Integer id;
    private String firstName;
    private String lastName;
    private List<CourseEntity> courseList;
}
