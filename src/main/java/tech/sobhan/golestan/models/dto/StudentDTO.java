package tech.sobhan.golestan.models.dto;

import lombok.*;

@Builder
@Getter
@RequiredArgsConstructor
@ToString
public class StudentDTO {
    private final Long id;
    private final String name;
    private final String number;
    private final Double score;
}
