package com.aker.TechNews.Service.apinews;

import com.aker.TechNews.Repository.ArticleRepository;
import com.aker.TechNews.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DataLoader implements Runnable{

    private int API_CALLS_ITERATION = 1;

    private int API_CALLS_LIMIT = 200;

    private int MAX_ARTICLES_TO_LOAD = 2000;

    private int ARTICLES_PER_PAGE = 10;

    private int PAUSE_TIME = 900000;

    @Autowired
    private NewsApi newsApi;

    @Autowired
    private ArticleRepository articleRepository;

    private Long nextPage = null;

    @Override
    @Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000)
    public void run() {
        deleteOldArticles();
        try {
            updateDatabase(PAUSE_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDatabase(int pauseTime) throws InterruptedException {
        int counter = 0;
        while(counter < API_CALLS_ITERATION){
            List<ResponseModel> response = newsApi.getArticles(nextPage);
            ResponseModel result = response.get(0);
            API_CALLS_ITERATION = getNumberOfIterations(result.getTotalResults());
            result.getResults().stream().forEach(article -> {
                articleRepository.insert(article);
            });
            nextPage = result.getNextPage();
            counter++;
            log.info("API_CALLS_ITERATION = " + counter);
            if(counter % 10 == 0) Thread.sleep(pauseTime);
        }
        log.info("Articles stored in database");
    }

    public int getNumberOfIterations(int totalResults){
        if(totalResults > MAX_ARTICLES_TO_LOAD) return API_CALLS_LIMIT;
        return totalResults / ARTICLES_PER_PAGE;
    }

    @CacheEvict(value = {"articles","articlesBySearch"})
    public void deleteOldArticles(){
        articleRepository.deleteAll();
        log.info("Cache evicted successfully");
    }


}
