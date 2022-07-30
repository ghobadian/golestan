package tech.sobhan.golestan.business.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super("ERROR404 User not found");
    }
}
