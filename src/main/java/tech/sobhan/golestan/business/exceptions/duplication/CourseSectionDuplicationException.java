package tech.sobhan.golestan.business.exceptions.duplication;

public class CourseSectionDuplicationException extends RuntimeException {
    public CourseSectionDuplicationException() {
        super("ERROR CourseSection already exists.");
    }
}