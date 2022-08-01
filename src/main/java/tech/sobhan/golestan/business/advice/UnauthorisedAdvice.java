package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.UnauthorisedException;

@ControllerAdvice
public class UnauthorisedAdvice {
    @ResponseBody
    @ExceptionHandler(UnauthorisedException.class)
    public String handler(UnauthorisedException ex) {
        return ex.getMessage();
    }
}
