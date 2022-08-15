package tech.sobhan.golestan.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.security.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private final PasswordEncoder passwordEncoder;
    private final Repo repo;
    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    private void loadAdmin() {
        if (adminExists()) {
            updateAdminsPasswordsBasedOnConfig();
        }else{
            createNewAdmin();
        }
    }

    private boolean adminExists() {
        return repo.userExistsByAdminPrivilege();
    }

    private void createNewAdmin() {
        User admin = User.builder().username(adminUsername).password(passwordEncoder.hash(adminPassword)).name("admin").phone("1234")
                .nationalId("1234").admin(true).active(true).build();
        repo.saveUser(admin);
    }

    private void updateAdminsPasswordsBasedOnConfig() {
        List<User> admins = repo.findUserByAdminPrivilege();
        admins.forEach(admin -> {
            admin.setPassword(adminPassword);
            admin.setUsername(adminUsername);
            repo.saveUser(admin);
        });
    }
}
