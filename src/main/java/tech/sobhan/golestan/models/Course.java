package tech.sobhan.golestan.models;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"title", "units"})
@ToString(of = {"title", "units"})
@Entity
@Table(indexes = @Index(columnList = "title"))
public class Course {
    @Id 
    @GeneratedValue
    private Long id;
    private String title;
    private int units;
}
