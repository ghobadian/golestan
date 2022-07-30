package tech.sobhan.golestan.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfiguration {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10);

    @Bean
    public static  PasswordEncoder getPasswordEncoder(){
        return PASSWORD_ENCODER;
    }

}
