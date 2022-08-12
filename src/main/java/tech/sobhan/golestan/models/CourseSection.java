package tech.sobhan.golestan.models;

import lombok.*;
import tech.sobhan.golestan.models.users.Instructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
public class CourseSection {
    @Id 
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Term term;
    @ManyToOne
    private Instructor instructor;
    @ManyToOne
    private Course course;
}
