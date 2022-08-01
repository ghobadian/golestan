package tech.sobhan.golestan.business.exceptions.duplication;

public class UserDuplicationException extends RuntimeException {
    public UserDuplicationException(){
        super("ERROR User already exists.");
    }
}