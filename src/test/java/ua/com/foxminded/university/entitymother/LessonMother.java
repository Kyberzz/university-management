package ua.com.foxminded.university.entitymother;

import java.time.LocalDate;

import ua.com.foxminded.university.entity.Lesson;

public class LessonMother {
    
    public static final int FIRST_LESSON = 1;
    public static final String SCHEDULE_DESCRIPTION = "some timetable description";
    public static final LocalDate DATE = LocalDate.of(2023, 5, 24);
    
    public static Lesson.LessonBuilder complete() {
        return Lesson.builder().datestamp(DATE)
                               .lessonOrder(FIRST_LESSON)
                               .description(SCHEDULE_DESCRIPTION);
    }
}
