package tech.sobhan.golestan.security;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    public String hash(String password) {
        StringBuilder passwordStringBuilder = new StringBuilder(password);
        passwordStringBuilder.reverse();
        StringBuilder output = new StringBuilder();
        for(int i=0;i < passwordStringBuilder.length();i++) {
            output
                    .append((char)('a' + i))
                    .append(passwordStringBuilder.charAt(i))
                    .append((char)('!' + (i%10)));
        }
        return output.toString();
    }
}
