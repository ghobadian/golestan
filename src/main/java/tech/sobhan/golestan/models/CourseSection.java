package tech.sobhan.golestan.models;

import lombok.*;
import tech.sobhan.golestan.models.users.Instructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
@EqualsAndHashCode
public class CourseSection {
    @Id @GeneratedValue private Long id;
    @ManyToOne
    private Term term;
    @ManyToOne
    private Instructor instructor;
    @ManyToOne
    private Course course;

    @Override
    public CourseSection clone(){
        return CourseSection.builder().course(course).instructor(instructor)
                .term(term).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseSection that = (CourseSection) o;
        return Objects.equals(term, that.term) && Objects.equals(instructor, that.instructor) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, instructor, course);
    }
}
