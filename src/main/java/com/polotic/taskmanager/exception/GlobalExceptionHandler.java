package com.polotic.taskmanager.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException() {
        return "error/400";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundException() {
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleInternalServerErrorException(Exception ex) {
        return "error/500";
    }

}
