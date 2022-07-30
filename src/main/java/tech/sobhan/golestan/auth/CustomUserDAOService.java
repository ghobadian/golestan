package tech.sobhan.golestan.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class CustomUserDAOService implements CustomUserDAO {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDAOService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectCustomUserDetailsByUsername(String username) {
        return getCustomUsers()
                .stream()
                .filter(customUser -> customUser.getUsername().equals(username))
                .findFirst();
    }

    private List<User> getCustomUsers(){
        return userRepository.findAll();
//        return Lists.newArrayList(
//                User.builder()
//                        .username("msghobadian")
//                        .password(passwordEncoder.encode("password"))
//                        .grantedAuthorities(STUDENT.getGrantedAuthorities())
//                        .isAccountNonExpired(true)
//                        .isCredentialsNonExpired(true)
//                        .isAccountNonLocked(true)
//                        .isEnabled(true)
//                        .build(),
//                User.builder()
//                        .username("admin")
//                        .password(passwordEncoder.encode("ramze_obur"))
//                        .grantedAuthorities(ADMIN.getGrantedAuthorities())
//                        .isAccountNonExpired(true)
//                        .isCredentialsNonExpired(true)
//                        .isAccountNonLocked(true)
//                        .isEnabled(true)
//                        .build()
//        );
    }
}
