package ua.com.foxminded.university.entity;


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
public class TimetableEntity {

    @NonNull private Integer id;
    private GroupEntity group;
    private CourseEntity course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayEntity weekDay;
}
