package tech.sobhan.golestan.models.users;

import lombok.*;
import tech.sobhan.golestan.enums.Degree;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity @Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Student{
    @Id 
    @GeneratedValue
    private Long id;
    private Degree degree;
    private Date startDate;
}
