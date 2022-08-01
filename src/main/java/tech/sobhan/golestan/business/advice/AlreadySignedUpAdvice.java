package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.AlreadySignedUpException;

@ControllerAdvice
public class AlreadySignedUpAdvice {

    @ResponseBody
    @ExceptionHandler(AlreadySignedUpException.class)
    public String handler(AlreadySignedUpException ex) {
        return ex.getMessage();
    }
}
