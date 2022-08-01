package tech.sobhan.golestan.business.advice.duplication;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.duplication.CourseSectionDuplicationException;

@ControllerAdvice
public class CourseSectionDuplicationAdvice {

    @ResponseBody
    @ExceptionHandler(CourseSectionDuplicationException.class)
    public String handler(CourseSectionDuplicationException ex) {
        return ex.getMessage();
    }
}