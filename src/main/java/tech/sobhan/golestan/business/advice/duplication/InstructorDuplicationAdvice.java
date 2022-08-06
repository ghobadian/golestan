package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.duplication.InstructorDuplicationException;

@ControllerAdvice
public class InstructorDuplicationAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(InstructorDuplicationException.class)
    public String handler(InstructorDuplicationException ex) {
        return ex.getMessage();
    }
}