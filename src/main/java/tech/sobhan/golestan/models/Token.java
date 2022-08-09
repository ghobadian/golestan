package tech.sobhan.golestan.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(of = "token")
@Entity
public class Token {
    @Id private String username;
    private String token;
}
