package tech.sobhan.golestan.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course {
    @Id @GeneratedValue private Long id;
    private String title;
    private int units;

    @Override
    public Course clone(){
        return Course.builder().title(title).units(units).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return units == course.units && Objects.equals(title, course.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, units);
    }

    @Override
    public String toString() {
        return "Course(title="+title+" , units="+units+")";
    }
}
