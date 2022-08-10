package tech.sobhan.golestan.business.exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException() {
        super("ERROR 403: you don't have permission to this section");
    }
}
