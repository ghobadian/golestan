package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.duplication.TermDuplicationException;

@ControllerAdvice
public class TermDuplicationAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(TermDuplicationException.class)
    public String handler(TermDuplicationException ex) {
        return ex.getMessage();
    }
}