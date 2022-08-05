package tech.sobhan.golestan.business.exceptions;

public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException(){
        super("The phone number that you entered is invalid.\n It should look like 09031095468");
    }
}
