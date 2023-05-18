package ua.com.foxminded.university.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LessonOrder {
    FIRST_LESSON(1), SECOND_LESSON(2), THIRD_LESSON(3), FOURTH_LESSON(4), FIFTH_LESSON(5);
    
    private final int representation;
    
    public static LessonOrder of(int representation) {
        for (LessonOrder element : LessonOrder.values()) {
            if (element.getRepresentation() == representation) {
                return element;
            }
        }
        return null;
    }
}