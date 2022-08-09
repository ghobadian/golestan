package tech.sobhan.golestan.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.enums.Rank;

@Component
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class InstructorDTO {
    private String name;
    private Rank rank;
}
