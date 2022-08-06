package tech.sobhan.golestan.business.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.ForbiddenException;

@ControllerAdvice
public class ForbiddenAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    public String handler(ForbiddenException ex) {
        return ex.getMessage();
    }
}