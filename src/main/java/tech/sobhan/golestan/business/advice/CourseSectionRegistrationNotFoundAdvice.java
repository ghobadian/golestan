package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.CourseSectionRegistrationNotFoundException;

@ControllerAdvice
public class CourseSectionRegistrationNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CourseSectionRegistrationNotFoundException.class)
    public String handler(CourseSectionRegistrationNotFoundException ex) {
        return ex.getMessage();
    }
}