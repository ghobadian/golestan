package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.InvalidPhoneNumberException;

@ControllerAdvice
public class InvalidPhoneNumberAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidPhoneNumberException.class)
    public String handler(InvalidPhoneNumberException ex) {
        return ex.getMessage();
    }
}
