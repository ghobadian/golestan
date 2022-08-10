package tech.sobhan.golestan.business.exceptions.duplication;

public class InstructorDuplicationException extends RuntimeException {
    public InstructorDuplicationException() {
        super("ERROR Instructor already exists.");
    }
}