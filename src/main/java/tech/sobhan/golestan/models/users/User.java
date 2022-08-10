package tech.sobhan.golestan.models.users;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
@ToString(of = {"username", "name", "phone", "nationalId"})
@EqualsAndHashCode(of = {"username", "phone", "nationalId"})
@Entity @Table(name = "users", indexes = @Index(columnList = "username"))
public class User {
    @Id 
    @GeneratedValue
    private Long id;
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
}
