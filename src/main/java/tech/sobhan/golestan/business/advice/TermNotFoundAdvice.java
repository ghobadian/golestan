package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.TermNotFoundException;

@ControllerAdvice
public class TermNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(TermNotFoundException.class)
    public String handler(TermNotFoundException ex) {
        return ex.getMessage();
    }
}