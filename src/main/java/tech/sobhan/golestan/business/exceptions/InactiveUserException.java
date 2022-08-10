package tech.sobhan.golestan.business.exceptions;

public class InactiveUserException extends RuntimeException{
    public InactiveUserException() {
        super("You are not activated by admin yet");
    }
}
