package tech.sobhan.golestan.business.exceptions;

public class CourseSectionRegistrationNotEmpty extends RuntimeException {
    public CourseSectionRegistrationNotEmpty(){
        super("ERROR403 registrations found for this course section, therefore deletion not allowed");
    }
}
