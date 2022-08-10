package tech.sobhan.golestan.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TermDTO {
    private Long termId;
    private String termTitle;
    private Double studentAverage;

}
