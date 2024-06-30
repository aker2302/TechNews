package com.aker.TechNews.Controller;

import com.aker.TechNews.Repository.UserRepository;
import com.aker.TechNews.Service.ArticleService;
import com.aker.TechNews.entity.Article;
import com.aker.TechNews.entity.User;
import com.aker.TechNews.model.UserModel;
import com.aker.TechNews.response.ApiResponseHandler;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/news")
public class TechNewsController {

    private static final Logger log = LoggerFactory.getLogger(TechNewsController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserRepository userRepository;

    @RateLimiter(name = "TechNewsLimiter", fallbackMethod = "getArticlesFallbackMethod")
    @GetMapping("/articles/pagination")
    public ResponseEntity<Object> fetchAllArticlesWithPagination(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size){
        if(size>10)
            return ResponseEntity.status(406).body("You cannot fetch more than 10 articles  per request");

        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articles = articleService.getArticlesByPage(pageable);

        return ApiResponseHandler.handlePageableApiResponse(articles.getNumber(), articles.getTotalPages(), HttpStatus.OK, articles.getContent(), articles.getTotalElements());
    }

    @RateLimiter(name = "TechNewsLimiter", fallbackMethod = "getArticlesFallbackMethod")
    @GetMapping("/articles/search")
    public ResponseEntity<Object> fetchArticlesBySearch(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam List<String> words){
        if(size>10)
            return ResponseEntity.status(406).body("You cannot fetch more than 10 articles  per request");

        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articles = articleService.getArticlesBySearch(pageable, words);

        return ApiResponseHandler.handlePageableApiResponse(articles.getNumber(), articles.getTotalPages(), HttpStatus.OK, articles.getContent(), articles.getTotalElements());
    }

    @RateLimiter(name = "TechNewsLimiter", fallbackMethod = "getArticlesFallbackMethod")
    @GetMapping("/articles/sorting")
    public ResponseEntity<Object> fetchArticlesBySoring(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam  String[] sort){
        if(size>10)
            return ResponseEntity.status(406).body("You cannot fetch more than 10 articles per request");

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort sortBy = Sort.by(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortBy);
        Page<Article> articles = articleService.getArticlesByPage(pageable);

        return ApiResponseHandler.handlePageableApiResponse(articles.getNumber(), articles.getTotalPages(), HttpStatus.OK, articles.getContent(), articles.getTotalElements());
    }

    public ResponseEntity<Object> getArticlesFallbackMethod(RequestNotPermitted requestNotPermitted){
        log.info("Fallback method invoked");
        log.info("Request not permitted : {}", requestNotPermitted.getMessage());
        return ResponseEntity.status(406).body("You Exceeded number of request available in 15min");
    }

    @PostMapping("/user/create")
    public ResponseEntity<Object> createUser(@RequestBody UserModel userModel){
        User user = userRepository.findByEmail(userModel.getEmail()).isPresent() ? userRepository.findByEmail(userModel.getEmail()).get() : null;
        if(user != null){
            return ResponseEntity.status(409).body("User already exist");
        }
        user = new User();
        user.setName(userModel.getName());
        user.setEmail(userModel.getEmail());
        String apikey = UUID.randomUUID().toString();
        user.setApiKey(apikey);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Here is your api Key: " + apikey);
    }

}
