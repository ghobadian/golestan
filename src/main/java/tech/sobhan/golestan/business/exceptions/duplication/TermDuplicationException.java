package tech.sobhan.golestan.business.exceptions.duplication;

public class TermDuplicationException extends RuntimeException {
    public TermDuplicationException() {
        super("ERROR Term already exists.");
    }
}