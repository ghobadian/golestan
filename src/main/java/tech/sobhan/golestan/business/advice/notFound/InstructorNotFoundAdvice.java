package tech.sobhan.golestan.business.advice.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.notFound.InstructorNotFoundException;

@ControllerAdvice
public class InstructorNotFoundAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(InstructorNotFoundException.class)
    public String handler(InstructorNotFoundException ex) {
        return ex.getMessage();
    }
}