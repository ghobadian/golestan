package tech.sobhan.golestan.business.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(){
        super("ERROR404 Instructor not found");
    }
}
