package com.aker.TechNews.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
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
        response.put("articles", responseObject);
        response.put("currentPage", currentPage);
        response.put("totalArticles", size);
        response.put("totalPages", totalPages);
        response.put("status", status);


        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> handlePageableSearchApiResponse(
            int currentPage,
            int totalPages,
            HttpStatus status,
            Object responseObject,
            Long size,
            List<String> keywords
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("articles", responseObject);
        response.put("keywords", keywords);
        response.put("currentPage", currentPage);
        response.put("totalArticles", size);
        response.put("totalPages", totalPages);
        response.put("status", status);


        return new ResponseEntity<>(response, status);
    }
}
