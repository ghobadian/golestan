package tech.sobhan.golestan.models.users;

import lombok.*;
import lombok.experimental.SuperBuilder;
import tech.sobhan.golestan.enums.Degree;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student{
    @Id @GeneratedValue private Long studentId;//todo change it to id
    private Degree degree;
    private Date startDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
