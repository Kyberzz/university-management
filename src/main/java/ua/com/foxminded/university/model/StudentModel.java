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
public class StudentModel {

    @NonNull private int id;
    private GroupModel group;
    private String firstName;
    private String lastName;
}
