package tech.sobhan.golestan.business.exceptions;

public class PageNumberException extends RuntimeException{
    public PageNumberException(){
        super("ERROR specified page number is not available.");
    }
}
