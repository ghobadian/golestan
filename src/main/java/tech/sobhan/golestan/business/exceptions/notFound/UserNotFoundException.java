package tech.sobhan.golestan.business.exceptions.notFound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("ERROR404 User not found");
    }
}
