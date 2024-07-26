package com.aker.TechNews.Controller;

import com.aker.TechNews.model.SubModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequestMapping("api/v1")
public class ApiProxyController {

    private static final Logger log = LoggerFactory.getLogger(ApiProxyController.class);
    private String apiKey = System.getenv("API_KEY_APP");

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String host = System.getenv("HOST");

    @GetMapping("/public/getarticles")
    public ResponseEntity<Object> serveArticles(@RequestParam String keywords,
                                                @RequestParam(defaultValue = "10")@Range(min = 0, max = 10) int pageSize,
                                                @RequestParam(defaultValue = "0") int page){
        String apiUrl;
        if (keywords == null || keywords.isEmpty()) {
            apiUrl = host + "/articles/pagination?" + "pageSize=" + pageSize + "&page=" + page ;
        } else {
            apiUrl = host + "/articles/search?page=" + page + "&pageSize=" + pageSize + "&words=" + keywords;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Object.class);
            return response;
        } catch (HttpClientErrorException e) {
            // Log the error and return appropriate response
            log.error("API request failed with status code: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (HttpStatusCodeException e) {
            // Log the error and return appropriate response
            log.error("API request failed with status code: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            // Log the error and return appropriate response
            log.error("API request failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/public/subscribe")
    public ResponseEntity<Object> subUSerToNewsLetter(@RequestBody SubModel subModel){
        String apiUrl = host + "/NewsLetter/subscription";
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-KEY", apiKey);
        headers.set("Content-Type", "application/json");

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(subModel);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert SubModel to JSON", e);
            return ResponseEntity.status(500).body("Failed to process the subscription request.");
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Object.class);
            return response;
        } catch (HttpClientErrorException e) {
            log.error("API request failed with status code: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (HttpStatusCodeException e) {
            log.error("API request failed with status code: {}", e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("API request failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("An error occurred while processing the subscription request.");
        }
    }
}
