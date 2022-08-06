package tech.sobhan.golestan.business.advice.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.notFound.CourseSectionRegistrationNotFoundException;

@ControllerAdvice
public class CourseSectionRegistrationNotFoundAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(CourseSectionRegistrationNotFoundException.class)
    public String handler(CourseSectionRegistrationNotFoundException ex) {
        return ex.getMessage();
    }
}