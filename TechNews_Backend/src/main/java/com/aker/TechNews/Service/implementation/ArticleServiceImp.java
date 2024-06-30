package com.aker.TechNews.Service.implementation;

import com.aker.TechNews.Repository.ArticleRepository;
import com.aker.TechNews.Service.ArticleService;
import com.aker.TechNews.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImp implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Cacheable("articles")
    @Override
    public Page<Article> getArticlesByPage(Pageable pageable) {
        Page<Article> Articles = articleRepository.findAll(pageable);
        return Articles;
    }

    @Cacheable("articlesBySearch")
    @Override
    public Page<Article> getArticlesBySearch(Pageable pageable, List<String> keywords){
        String words = keywords.stream()
                .map(keyword -> "(?i).*" + keyword + ".*")
                .collect(Collectors.joining("|"));
        Page<Article> articles = articleRepository.searchByKeywords(words, pageable);
        return articles;
    }



}
