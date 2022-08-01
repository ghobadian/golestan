package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.InstructorNotFoundException;

@ControllerAdvice
public class InstructorNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(InstructorNotFoundException.class)
    public String handler(InstructorNotFoundException ex) {
        return ex.getMessage();
    }
}