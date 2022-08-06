package tech.sobhan.golestan.business.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tech.sobhan.golestan.business.exceptions.PageNumberException;

@ControllerAdvice
public class PageNumberAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(PageNumberException.class)
    public String handler(PageNumberException ex) {
        return ex.getMessage();
    }
}