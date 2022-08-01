package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.ForbiddenException;

@ControllerAdvice
public class ForbiddenAdvice {

    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    public String handler(ForbiddenException ex) {
        return ex.getMessage();
    }
}