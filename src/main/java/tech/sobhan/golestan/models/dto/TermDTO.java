package tech.sobhan.golestan.models.dto;

import lombok.*;

@Builder
@Getter
@RequiredArgsConstructor
@ToString
public class TermDTO {
    private final Long termId;
    private final String termTitle;
    private final Double studentAverage;

}
