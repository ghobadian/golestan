package tech.sobhan.golestan.business.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.InactiveUserException;

@ControllerAdvice
public class InactiveUserAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(InactiveUserException.class)
    public String handler(InactiveUserException ex) {
        return ex.getMessage();
    }
}