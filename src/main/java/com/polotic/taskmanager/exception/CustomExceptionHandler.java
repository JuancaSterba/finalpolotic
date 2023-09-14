package com.polotic.taskmanager.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

public class CustomExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof BadRequestException) {
            return new ModelAndView("error/400");
        } else if (ex instanceof NoHandlerFoundException) {
            return new ModelAndView("error/404");
        }else if (ex instanceof Exception) {
            return new ModelAndView("error/500");
        }
        return null;
    }

}
