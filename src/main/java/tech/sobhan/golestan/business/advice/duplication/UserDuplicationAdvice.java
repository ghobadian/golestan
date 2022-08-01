package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.duplication.UserDuplicationException;

@ControllerAdvice
public class UserDuplicationAdvice {
    @ResponseBody
    @ExceptionHandler(UserDuplicationException.class)
    public String handler(UserDuplicationException ex) {
        return ex.getMessage();
    }
}