package tech.sobhan.golestan.business.exceptions;

public class UnauthorisedException extends RuntimeException{
    public UnauthorisedException(){
        super("You are not an eligible User");
    }
}
