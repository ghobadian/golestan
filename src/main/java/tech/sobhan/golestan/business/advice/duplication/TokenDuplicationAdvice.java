package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.duplication.TokenDuplicationException;

@ControllerAdvice
public class TokenDuplicationAdvice {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(TokenDuplicationException.class)
    public String handler(TokenDuplicationException ex) {
        return ex.getMessage();
    }
}
