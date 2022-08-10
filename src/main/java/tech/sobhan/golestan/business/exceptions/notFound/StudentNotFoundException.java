package tech.sobhan.golestan.business.exceptions.notFound;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException() {
        super("ERROR404 Student not found");
    }
}
