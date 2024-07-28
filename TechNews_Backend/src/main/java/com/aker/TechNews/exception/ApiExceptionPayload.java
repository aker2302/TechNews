package com.aker.TechNews.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiExceptionPayload {
    private final String message;
    private final HttpStatus Status;
    private final ZonedDateTime zonedDateTime;

    public ApiExceptionPayload(String message, HttpStatus status, ZonedDateTime zonedDateTime) {
        this.message = message;
        Status = status;
        this.zonedDateTime = zonedDateTime;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return Status;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}
