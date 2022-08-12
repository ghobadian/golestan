package tech.sobhan.golestan.business.exceptions.notFound;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException() {
        super("Token not found");
    }
}
