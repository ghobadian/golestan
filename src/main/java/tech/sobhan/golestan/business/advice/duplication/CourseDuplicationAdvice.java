package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.duplication.CourseDuplicationException;

@ControllerAdvice
public class CourseDuplicationAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(CourseDuplicationException.class)
    public String handler(CourseDuplicationException ex) {
        return ex.getMessage();
    }
}