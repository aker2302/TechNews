package com.aker.TechNews.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class NewsApiExceptionHandler {

    @ExceptionHandler(value = {NewsApiException.class})
    public ResponseEntity<Object> handleApiRequestException(NewsApiException e){
        ApiExceptionPayload apiExceptionPayload = new ApiExceptionPayload
                (e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiExceptionPayload, HttpStatus.BAD_REQUEST);
    }
}
