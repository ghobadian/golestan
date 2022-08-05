package tech.sobhan.golestan.models.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "phone", nullable = false, updatable = false, unique = true)
    private String phone;
    @Column(name = "national_id", nullable = false, updatable = false, unique = true)
    private String nationalId;
    @Column(name = "admin", nullable = false)
    private boolean admin;
    @Column(name = "active", nullable = false)
    private boolean active;
    @OneToOne
    @JoinColumn(name = "student", unique = true)
    private Student student = null;
    @OneToOne
    @JoinColumn(name = "instructor", unique = true)
    private Instructor instructor = null;

    @Override
    public int hashCode() {
        return Objects.hash(username, phone, nationalId);
    }

    @Override
    public User clone(){
        return User.builder().username(username).password(password).name(name).phone(phone)
                .nationalId(nationalId).admin(admin).active(active).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(phone, user.phone) && Objects.equals(nationalId, user.nationalId);
    }
}
