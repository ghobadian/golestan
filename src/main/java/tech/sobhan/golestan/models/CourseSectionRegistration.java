package tech.sobhan.golestan.models;

import lombok.*;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.users.Student;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
@EqualsAndHashCode
public class CourseSectionRegistration {
    @Id @GeneratedValue private Long id;
    private double score;
    @ManyToOne
    private CourseSection courseSection;
    @ManyToOne
    private Student student;
}
