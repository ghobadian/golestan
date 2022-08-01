package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.duplication.InstructorDuplicationException;

@ControllerAdvice
public class InstructorDuplicationAdvice {

    @ResponseBody
    @ExceptionHandler(InstructorDuplicationException.class)
    public String handler(InstructorDuplicationException ex) {
        return ex.getMessage();
    }
}