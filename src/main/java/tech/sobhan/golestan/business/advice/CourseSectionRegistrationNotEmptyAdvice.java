package tech.sobhan.golestan.business.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.CourseSectionRegistrationNotEmptyException;

@ControllerAdvice
public class CourseSectionRegistrationNotEmptyAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(CourseSectionRegistrationNotEmptyException.class)
    public String handler(CourseSectionRegistrationNotEmptyException ex) {
        return ex.getMessage();
    }
}