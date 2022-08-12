package tech.sobhan.golestan.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.security.PasswordEncoder;

import javax.annotation.PostConstruct;


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
        deleteAllAdmins(repo);
        User admin = User.builder().username(adminUsername).password((adminPassword)).name("admin").phone("1234")
                .nationalId("1234").admin(true).active(true).build();
        admin.setPassword(passwordEncoder.hash(admin.getPassword()));
        repo.saveUser(admin);
    }

    private void deleteAllAdmins(Repo repo) {
        if(repo.userExistsByAdminPrivilege()) {
            repo.deleteUsersWithAdminPrivilege();
        }
    }
}
