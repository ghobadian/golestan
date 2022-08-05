package tech.sobhan.golestan.business.exceptions;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(){
        super("You are not active yet.");
    }
}
