package tech.sobhan.golestan.security;

public class PasswordEncoder {
    public static String hash(String password){
        StringBuilder passwordStringBuilder = new StringBuilder(password);
        passwordStringBuilder.reverse();
        StringBuilder output = new StringBuilder();
        for(int i=0;i < passwordStringBuilder.length();i++){
            output
                    .append((char)('a' + i))
                    .append(passwordStringBuilder.charAt(i))
                    .append((char)('!' + (i%10)));
        }
        return output.toString();
    }
}
