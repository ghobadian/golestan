package tech.sobhan.golestan.business.advice.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.notFound.StudentNotFoundException;

@ControllerAdvice
public class StudentNotFoundAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(StudentNotFoundException.class)
    public String handler(StudentNotFoundException ex) {
        return ex.getMessage();
    }
}