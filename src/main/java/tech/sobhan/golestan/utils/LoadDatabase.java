package tech.sobhan.golestan.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.Repository;

import static tech.sobhan.golestan.security.PasswordEncoder.hash;

@Configuration
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(Repository repository){
        return args -> loadAdmin(repository);
    }

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;
    private void loadAdmin(Repository repository) {
        if(repository.userExistsByAdminPrivilege()){
            repository.deleteUsersWithAdminPrivilege();
        }
        User admin = User.builder().username(adminUsername).password((adminPassword)).name("admin").phone("1234")
                .nationalId("1234").admin(true).active(true).build();
        admin.setPassword(hash(admin.getPassword()));
        repository.saveUser(admin);
    }
}
