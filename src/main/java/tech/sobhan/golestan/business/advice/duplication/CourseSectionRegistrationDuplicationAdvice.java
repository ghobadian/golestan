package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.duplication.CourseSectionRegistrationDuplicationException;

@ControllerAdvice
public class CourseSectionRegistrationDuplicationAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(CourseSectionRegistrationDuplicationException.class)
    public String handler(CourseSectionRegistrationDuplicationException ex) {
        return ex.getMessage();
    }
}