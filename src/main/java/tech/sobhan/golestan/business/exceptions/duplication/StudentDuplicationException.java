package tech.sobhan.golestan.business.exceptions.duplication;

public class StudentDuplicationException extends RuntimeException {
    public StudentDuplicationException() {
        super("ERROR Student already exists.");
    }
}