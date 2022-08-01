package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.DuplicateUserException;

@ControllerAdvice
public class DuplicateUserAdvice {

    @ResponseBody
    @ExceptionHandler(DuplicateUserException.class)
    public String handler(DuplicateUserException ex) {
        return ex.getMessage();
    }
}