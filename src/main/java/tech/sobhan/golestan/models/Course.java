package tech.sobhan.golestan.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"title", "units"})
@ToString(of = {"title", "units"})
@Entity
public class Course {
    @Id @GeneratedValue private Long id;
    private String title;
    private int units;
}
