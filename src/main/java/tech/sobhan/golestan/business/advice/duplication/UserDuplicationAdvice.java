package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.duplication.UserDuplicationException;

@ControllerAdvice
public class UserDuplicationAdvice {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(UserDuplicationException.class)
    public String handler(UserDuplicationException ex) {
        return ex.getMessage();
    }
}