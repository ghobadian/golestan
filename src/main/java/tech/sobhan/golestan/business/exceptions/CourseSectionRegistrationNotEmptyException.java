package tech.sobhan.golestan.business.exceptions;

public class CourseSectionRegistrationNotEmptyException extends RuntimeException {
    public CourseSectionRegistrationNotEmptyException(){
        super("ERROR403 registrations found for this course section, therefore deletion not allowed");
    }
}
