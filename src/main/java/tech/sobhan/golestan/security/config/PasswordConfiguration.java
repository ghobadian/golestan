package tech.sobhan.golestan.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
public class PasswordConfiguration {
    private static PasswordEncoder PASSWORD_ENCODER = null;

    public PasswordConfiguration(){
    }

    @Bean
    public static  PasswordEncoder getPasswordEncoder(){
        if(PASSWORD_ENCODER==null){
            PASSWORD_ENCODER = new BCryptPasswordEncoder();
        }
        return PASSWORD_ENCODER;
    }

}
