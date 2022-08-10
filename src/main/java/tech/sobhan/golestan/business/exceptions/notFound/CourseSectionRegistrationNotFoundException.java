package tech.sobhan.golestan.business.exceptions.notFound;

public class CourseSectionRegistrationNotFoundException extends RuntimeException {
    public CourseSectionRegistrationNotFoundException() {
        super("ERROR404 course section registration not found");
    }
}
