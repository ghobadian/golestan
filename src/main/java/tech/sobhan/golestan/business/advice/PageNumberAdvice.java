package tech.sobhan.golestan.business.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.sobhan.golestan.business.exceptions.PageNumberException;

@ControllerAdvice
public class PageNumberAdvice {

    @ResponseBody
    @ExceptionHandler(PageNumberException.class)
    public String handler(PageNumberException ex) {
        return ex.getMessage();
    }
}