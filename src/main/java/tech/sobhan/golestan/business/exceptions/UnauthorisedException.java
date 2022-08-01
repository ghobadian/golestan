package tech.sobhan.golestan.business.exceptions;

public class UnauthorisedException extends RuntimeException{
    public UnauthorisedException(){//todo what if user was not active
        super("ERROR 401: You are not an eligible User");
    }
}
