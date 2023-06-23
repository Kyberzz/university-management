package ua.com.foxminded.university.exception;

import static javax.servlet.http.HttpServletResponse.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ServiceErrorCode {
    TEACHER_NOT_NULL_CONSTRAINT_VIOLATION(SC_INTERNAL_SERVER_ERROR, 
            "A teacher that has lessons cannot be removed"),
    
    PERSON_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to delete the person data"),
    PERSON_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to create the database person data"),
    PERSON_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to update the database person data"),
    PERSON_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the database person data"),
    
    USER_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to delete the user data"),
    USERS_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the users data"),
    USER_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the user data"),
    USER_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to update the user data"),
    USER_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to database persist the user data"),
    USER_EMAIL_DUPLICATION_ERROR(SC_BAD_REQUEST, "This email address already exists"),
    USER_AUTHORITY_CREATE_ERROR(SC_BAD_REQUEST, 
            "Failed to database persist the user authority data"),
    
    TIMING_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to database persist the timing"),
    TIMING_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to update the database timing data"),
    TIMING_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the database timing data"),
    
    TEACHER_UPDATE_ERORR(SC_INTERNAL_SERVER_ERROR, "Failed to update the database teacher data"),
    TEACHER_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to persist teacher data to the database"),
    TEACHER_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to delete the database teacher data"),
    TEACHERS_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch database teachers data"),
    TEACHER_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the database teacher data"),
    
    STUDETNS_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the database students data"),
    STUDENT_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the database student data"),
    STUDENT_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to database persist the student data"),
    STUDENT_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to update the database student data"),
    STUDENT_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to delete the database student data"),
    
    LESSON_PERSISTENCE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to database persiste the timetable data"),
    LESSON_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to delete the database lesson data"),
    LESSON_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to update the database lesson data"),
    LESSONS_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch database lessons data"), 
    LESSON_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to fetch the lesson data from the database"),
    
    GROUP_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to update data of the group in the database"),
    GROUP_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to create data of the group in the database"),
    GROUP_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to delete data of the group in the database"),
    GROUPS_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to fetch data of groups from the database"),
    GROUP_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to fetch data of the group from the database"),
    COURSE_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to update course data in the database"),
    COURSES_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to fetch courses data from the database"),
    COURSE_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to persiste course data in the database"),
    COURSE_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to delete the course data from the database"),
    COURSE_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to fetch the course data from the database"),
    API_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Internal server error"),
    TIMETABLE_DELETE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to delete the database timetable data"),
    TIMETABLE_CREATE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failded to updata the database timetable data"),
    TIMETABLE_UPDATE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to update the database timetable data"),
    TIMETABLES_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to fetch the database timetables data"),
    TIMETABLE_FETCH_ERROR(SC_INTERNAL_SERVER_ERROR, "Failed to fetch the database timetable data"),
    TIMETABLE_PERSISTENCE_ERROR(SC_INTERNAL_SERVER_ERROR, 
            "Failed to persiste timetable data in the database"),
    TIMETABLE_NAME_DUPLICATION(SC_BAD_REQUEST, 
            "This timetable name already exists");
    
    private final int code;
    private final String description;
}
