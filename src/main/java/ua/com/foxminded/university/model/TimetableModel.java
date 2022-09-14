package ua.com.foxminded.university.model;

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
public class TimetableModel {

    @NonNull private Integer id;
    private GroupModel group;
    private CourseModel course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayModel weekDay;
}
