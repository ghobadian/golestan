package tech.sobhan.golestan.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SummeryDTO {
    private List<TermDTO> termDetails;
    private double totalAverage;
}
