package tech.sobhan.golestan.business.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(){
        super("ERROR403 User with this property already Exists.");
    }
}
