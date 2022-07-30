package tech.sobhan.golestan.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
@Entity @Table(name = "users")
public class User implements UserDetails {
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
//    private Set<? extends GrantedAuthority> grantedAuthorities;

    @Override
    public int hashCode() {
        return Objects.hash(username, phone, nationalId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;//todo change later
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
