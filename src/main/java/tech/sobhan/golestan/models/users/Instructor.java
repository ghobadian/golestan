package tech.sobhan.golestan.models.users;

import lombok.*;
import tech.sobhan.golestan.enums.Rank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
