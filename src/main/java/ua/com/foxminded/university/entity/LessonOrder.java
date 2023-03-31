package ua.com.foxminded.university.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LessonOrder {
    FIRST_LESSON("1th"), SECOND_LESSON("2th"), THIRD_LESSON("3th"), FOURTH_LESSON("4th"), 
    FIFTH_LESSON("5th");
    
    private final String representaion;
}
