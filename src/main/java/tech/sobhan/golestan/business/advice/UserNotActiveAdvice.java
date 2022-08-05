package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.UserNotActiveException;

@ControllerAdvice
public class UserNotActiveAdvice {

    @ResponseBody
    @ExceptionHandler(UserNotActiveException.class)
    public String handler(UserNotActiveException ex) {
        return ex.getMessage();
    }
}