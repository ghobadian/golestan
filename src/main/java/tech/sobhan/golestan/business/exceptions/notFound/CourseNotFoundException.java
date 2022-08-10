package tech.sobhan.golestan.business.exceptions.notFound;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException() {
        super("ERROR404 course not found.");
    }
}
