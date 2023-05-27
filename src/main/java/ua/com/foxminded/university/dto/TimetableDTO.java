package ua.com.foxminded.university.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TimetableDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    @NotBlank
    private String name;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TimingDTO> timings;
}
