package tech.sobhan.golestan.models.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
@ToString
public class StudentAverageDTO {
    private final Double average;
    private final List<CourseSectionDTO> courseSections;
}
