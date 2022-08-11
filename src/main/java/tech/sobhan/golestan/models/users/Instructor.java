package tech.sobhan.golestan.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tech.sobhan.golestan.enums.Rank;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
@ToString(of = "rank")
@NoArgsConstructor
public class Instructor {
    @Id 
    @GeneratedValue
    private Long id;
    private Rank rank;
    private String name;
    @OneToOne(mappedBy = "instructor")
    @JsonIgnore
    private User user;
}
