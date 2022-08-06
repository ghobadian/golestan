package tech.sobhan.golestan.business.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.InvalidNationalIdException;

@ControllerAdvice
public class InvalidNationalIdAdvice {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(InvalidNationalIdException.class)
    public String handler(InvalidNationalIdException ex) {
        return ex.getMessage();
    }
}
