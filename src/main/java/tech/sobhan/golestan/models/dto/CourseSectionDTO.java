package tech.sobhan.golestan.models.dto;

import lombok.*;

@Builder
@Getter
@RequiredArgsConstructor
@ToString
public class CourseSectionDTO {
    private final Long id;
    private final String courseName;
    private final int courseUnits;
    private final InstructorDTO instructor;
    private final Double score;
}
