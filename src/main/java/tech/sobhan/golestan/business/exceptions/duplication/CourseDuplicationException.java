package tech.sobhan.golestan.business.exceptions.duplication;

public class CourseDuplicationException extends RuntimeException {
    public CourseDuplicationException(){
        super("ERROR Course already exists.");
    }
}