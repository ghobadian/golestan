package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.duplication.CourseDuplicationException;

@ControllerAdvice
public class CourseDuplicationAdvice {

    @ResponseBody
    @ExceptionHandler(CourseDuplicationException.class)
    public String handler(CourseDuplicationException ex) {
        return ex.getMessage();
    }
}