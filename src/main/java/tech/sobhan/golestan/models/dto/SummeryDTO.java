package tech.sobhan.golestan.models.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
@ToString
public class SummeryDTO {
    private final List<TermDTO> termDetails;
    private final double totalAverage;
}
