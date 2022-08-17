package tech.sobhan.golestan.models.dto;

import lombok.*;
import tech.sobhan.golestan.models.CourseSection;

@Builder
@Getter
@RequiredArgsConstructor
@ToString
public class CourseSectionDTOLight {
    private final CourseSection courseSection;
    private final int numberOfStudents;
}
