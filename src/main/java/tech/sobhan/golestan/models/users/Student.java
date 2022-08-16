package tech.sobhan.golestan.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tech.sobhan.golestan.enums.Degree;

import javax.persistence.*;
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
    @OneToOne(mappedBy = "student")
    @JsonIgnore
    private User user;
}
