package tech.sobhan.golestan.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tech.sobhan.golestan.models.CourseSection;

@Data
@AllArgsConstructor
@Builder
public class CourseSectionDTOLight {
    private CourseSection courseSection;
    private int numberOfStudents;
}
