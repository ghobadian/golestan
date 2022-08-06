package tech.sobhan.golestan.business.advice.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.notFound.TermNotFoundException;

@ControllerAdvice
public class TermNotFoundAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(TermNotFoundException.class)
    public String handler(TermNotFoundException ex) {
        return ex.getMessage();
    }
}