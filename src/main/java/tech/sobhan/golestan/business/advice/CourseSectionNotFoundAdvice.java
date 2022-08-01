package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.CourseSectionNotFoundException;

@ControllerAdvice
public class CourseSectionNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CourseSectionNotFoundException.class)
    public String handler(CourseSectionNotFoundException ex) {
        return ex.getMessage();
    }
}