package com.aker.TechNews.Controller;

import com.aker.TechNews.Repository.NewsLetterSubRepository;
import com.aker.TechNews.Service.ArticleService;
import com.aker.TechNews.Service.implementation.EmailSenderService;
import com.aker.TechNews.entity.Article;
import com.aker.TechNews.entity.NewsLetterSub;
import com.aker.TechNews.model.MailRequest;
import com.aker.TechNews.model.SubModel;
import com.aker.TechNews.response.ApiResponseHandler;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/news")
@SecurityRequirement(name = "API-Key")
@Tag(name = "Articles")
public class TechNewsController {

    private static final Logger log = LoggerFactory.getLogger(TechNewsController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private NewsLetterSubRepository newsLetterSubRepository;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Articles"),
            @ApiResponse(responseCode = "401", description= "Not authorized")
    })
    @Operation(description = "Allows to fetch articles by pages")
    @RateLimiter(name = "TechNewsLimiter", fallbackMethod = "getArticlesFallbackMethod")
    @GetMapping("/articles/pagination")
    public ResponseEntity<Object> fetchAllArticlesWithPagination(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10")@Range(min = 0, max = 10) int pageSize){
        if(pageSize>10)
            return ResponseEntity.status(406).body("\"You cannot fetch more than 10 articles  per request\"");
        try{
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Article> articles = articleService.getArticlesByPage(pageable);
            return ApiResponseHandler.handlePageableApiResponse(articles.getNumber(), articles.getTotalPages(), HttpStatus.OK, articles.getContent(), articles.getTotalElements());
        } catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Articles found"),
            @ApiResponse(responseCode = "401", description= "Not authorized"),
            @ApiResponse(responseCode = "406", description= "Invalid page size"),
            @ApiResponse(responseCode = "500", description= "Internal server error")
    })
    @Operation(description = "Allows to search in articles ")
    @RateLimiter(name = "TechNewsLimiter", fallbackMethod = "getArticlesFallbackMethod")
    @GetMapping("/articles/search")
    public ResponseEntity<Object> fetchArticlesBySearch(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10")@Range(min = 0, max = 10) int pageSize,
                                                        @RequestParam List<String> words){
        try {
            if (pageSize > 10) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body("\"You cannot fetch more than 10 articles per request\"");
            }

            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Article> articles = articleService.getArticlesBySearch(pageable, words);

            return ApiResponseHandler.handlePageableSearchApiResponse(articles.getNumber(),
                    articles.getTotalPages(), HttpStatus.OK, articles.getContent(),
                    articles.getTotalElements(), words);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "\"An error occurred while fetching articles\"", e);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Articles found"),
            @ApiResponse(responseCode = "401", description= "Not authorized"),
            @ApiResponse(responseCode = "406", description= "Invalid page size"),
            @ApiResponse(responseCode = "500", description= "Internal server error")
    })
    @Operation(description = "Allows to sort articles by parameter")
    @RateLimiter(name = "TechNewsLimiter", fallbackMethod = "getArticlesFallbackMethod")
    @GetMapping("/articles/sorting")
    public ResponseEntity<Object> fetchArticlesBySorting(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10")@Range(min = 0, max = 10) int pageSize,
                                                        @RequestParam String[] sort){
        try {
            if(pageSize>10)
                return ResponseEntity.status(406).body("\"You cannot fetch more than 10 articles per request\"");

            Sort.Direction direction = Sort.Direction.fromString(sort[1]);
            Sort sortBy = Sort.by(direction, sort[0]);
            Pageable pageable = PageRequest.of(page, pageSize, sortBy);
            Page<Article> articles = articleService.getArticlesByPage(pageable);

            return ApiResponseHandler.handlePageableApiResponse(articles.getNumber(),
                    articles.getTotalPages(),
                    HttpStatus.OK,
                    articles.getContent(),
                    articles.getTotalElements());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "\"An error occurred while fetching articles\"", e);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscribed to News Letter"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "409", description = "You already registered to the news letter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(description = "Allows user to subscribe to the news letter")
    @RateLimiter(name = "TechNewsLimiter", fallbackMethod = "getArticlesFallbackMethod")
    @PostMapping("/NewsLetter/subscription")
    public ResponseEntity<Object> subscribeToNewsLetter(@RequestBody SubModel subModel){
        try {
            Map<String, Object> model = new HashMap<>();
            MailRequest request = new MailRequest();
            NewsLetterSub user = newsLetterSubRepository.findByEmail(subModel.getEmail())
                    .orElse(null);

            if (user != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("\"You already registered to the news letter\"");
            }

            user = new NewsLetterSub(subModel.getName(), subModel.getEmail());
            request.setDestination(subModel.getEmail());
            request.setName(subModel.getName());
            request.setSubject("Subscribe to News Letter");
            request.setFrom("technews.newsletter@gmail.com");
            model.put("Name", subModel.getName());
            model.put("Email", subModel.getEmail());

            newsLetterSubRepository.save(user);
            emailSenderService.sendSubMail(request, model);

            return ResponseEntity.status(HttpStatus.OK).body("\"Thank you, You Subscribed to News Letter\"");

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "\"An error occurred while subscribing to the news letter\"", e);
        }
    }

    public ResponseEntity<Object> getArticlesFallbackMethod(RequestNotPermitted requestNotPermitted){
        log.info("Fallback method invoked");
        log.info("Request not permitted : {}", requestNotPermitted.getMessage());
        return ResponseEntity.status(406).body("\"You Exceeded number of request available in 15 min\"");
    }


}
