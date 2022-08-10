package tech.sobhan.golestan.business.exceptions.notFound;

public class CourseSectionNotFoundException extends RuntimeException {
    public CourseSectionNotFoundException() {
        super("ERROR404 Course section not found.");
    }
}
