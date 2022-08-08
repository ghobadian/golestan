package tech.sobhan.golestan.business.exceptions.duplication;

public class TokenDuplicationException extends RuntimeException {
    public TokenDuplicationException(){
        super("token with this username already exists");
    }
}
