package tech.sobhan.golestan.models.users;

import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.enums.Rank;

import javax.persistence.*;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class Instructor {
    @Id @GeneratedValue private Long id;
    private Rank rank;
    //todo add user id


//    @Override//todo fix later
//    public String toString() {
//        JSONObject output = new JSONObject();
//        JSONObject instructorDetails = new JSONObject();
//        User user = userRepository.
//        instructorDetails.put()
//        output.put("instructor", instructorDetails);
//        return "Instructor{" +
//                "id=" + id +
//                ", rank=" + rank +
//                '}';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instructor that = (Instructor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
