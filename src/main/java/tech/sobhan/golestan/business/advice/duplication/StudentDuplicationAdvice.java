package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.duplication.StudentDuplicationException;

@ControllerAdvice
public class StudentDuplicationAdvice {

    @ResponseBody
    @ExceptionHandler(StudentDuplicationException.class)
    public String handler(StudentDuplicationException ex) {
        return ex.getMessage();
    }
}