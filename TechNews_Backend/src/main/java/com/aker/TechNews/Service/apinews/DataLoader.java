package com.aker.TechNews.Service.apinews;

import com.aker.TechNews.Repository.ArticleRepository;
import com.aker.TechNews.entity.Article;
import com.aker.TechNews.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class DataLoader implements CommandLineRunner {


    @Autowired
    private NewsApi newsApi;

    @Autowired
    private ArticleRepository articleRepository;

    private Long nextPage = null;

    @Override
    public void run(String... args) throws Exception {
        int counter = 0;
        while(counter < 1){
            List<ResponseModel> response = newsApi.getArticles(nextPage);
            ResponseModel result = response.get(0);
            //log.info(result.toString());
            //log.info("Size = " + result.getResults().size() + " " + response.get(0).getNextPage());
            result.getResults().stream().forEach(article -> {
                articleRepository.insert(article);
            });
            nextPage = result.getNextPage();
            counter++;
        }
        log.info("Articles stored in database");
    }
}
