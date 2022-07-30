package tech.sobhan.golestan.business.exceptions;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(){
        super("ERROR404 Student not found");
    }
}
