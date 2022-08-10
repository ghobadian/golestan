package tech.sobhan.golestan.business.exceptions.notFound;

public class TermNotFoundException extends RuntimeException {
    public TermNotFoundException() {
        super("ERROR404 Term not found.");
    }
}
