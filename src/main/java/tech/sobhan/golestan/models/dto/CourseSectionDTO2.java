package tech.sobhan.golestan.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CourseSectionDTO2 {
    private Long id;
    private String courseName;
    private int courseUnits;
    private InstructorDTO instructor;
    private Double score;
}
