package tech.sobhan.golestan.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import tech.sobhan.golestan.enums.Rank;

@Builder
@Getter
@RequiredArgsConstructor
@ToString
public class InstructorDTO {
    private final String name;
    private final Rank rank;
}
