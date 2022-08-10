package tech.sobhan.golestan.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String name;
    private String number;
    private Double score;
}
