package tech.sobhan.golestan.business.exceptions.duplication;

public class CourseSectionRegistrationDuplicationException extends RuntimeException {
    public CourseSectionRegistrationDuplicationException() {
        super("ERROR CourseSectionRegistration already exists.");
    }
}