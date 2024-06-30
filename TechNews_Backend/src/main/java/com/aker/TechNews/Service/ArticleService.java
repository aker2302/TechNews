package com.aker.TechNews.Service;


import com.aker.TechNews.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    List<Article> getAllArticles();

    Page<Article> getArticlesByPage(Pageable pageable);

    Page<Article> getArticlesBySearch(Pageable pageable, List<String> keywords);
}
