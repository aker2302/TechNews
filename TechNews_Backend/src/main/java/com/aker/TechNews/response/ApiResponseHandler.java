package com.aker.TechNews.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ApiResponseHandler {

    public static ResponseEntity<Object> handlePageableApiResponse(
            int currentPage,
            int totalPages,
            HttpStatus status,
            Object responseObject,
            Long size
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("Articles", responseObject);
        response.put("currentPage", currentPage);
        response.put("totalArticles", size);
        response.put("totalPages", totalPages);
        response.put("Status", status);


        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> handleApiResponse(
            String message,
            HttpStatus status,
            Object responseObject,
            int size
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("Articles", responseObject);
        response.put("totalArticles", size);
        response.put("Status", status);


        return new ResponseEntity<>(response, status);
    }
}
