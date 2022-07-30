package tech.sobhan.golestan.auth;

import java.util.Optional;

public interface CustomUserDAO {
    Optional<User> selectCustomUserDetailsByUsername(String username);
}
