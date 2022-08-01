package tech.sobhan.golestan.business.exceptions;

public class AlreadySignedUpException extends RuntimeException {
    public AlreadySignedUpException(){
        super("ERROR You have already Signed up for this course.");
    }
}
