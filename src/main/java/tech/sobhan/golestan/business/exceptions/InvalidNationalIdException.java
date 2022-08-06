package tech.sobhan.golestan.business.exceptions;

public class InvalidNationalIdException extends RuntimeException {
    public InvalidNationalIdException(){
        super("The national Id is invalid\nit should look like 5412132546");
    }
}
