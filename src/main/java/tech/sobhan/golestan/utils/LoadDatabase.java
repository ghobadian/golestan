package tech.sobhan.golestan.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;

@Configuration
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(RepositoryHandler repositoryHandler){
        return args -> {
            loadAdmin(repositoryHandler);
        };
    }

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;
    private void loadAdmin(RepositoryHandler repositoryHandler) {
        if(repositoryHandler.userExistsByAdminPrivilege()){
            repositoryHandler.deleteUsersWithAdminPrivilege();
        }
        User admin = User.builder().username(adminUsername).password((adminPassword)).name("admin").phone("1234")
                .nationalId("1234").admin(true).active(true).build();
        repositoryHandler.saveUser(admin);
    }
}
