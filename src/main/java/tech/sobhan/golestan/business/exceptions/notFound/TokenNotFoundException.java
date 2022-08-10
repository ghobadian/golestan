package tech.sobhan.golestan.business.exceptions.notFound;

public class TokenNotFoundException extends Exception{//todo use exception instead of Runtime exception
    public TokenNotFoundException() {
        super("Token not found");
    }
}
