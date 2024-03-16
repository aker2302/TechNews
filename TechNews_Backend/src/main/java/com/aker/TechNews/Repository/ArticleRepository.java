package com.aker.TechNews.Repository;

import com.aker.TechNews.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {
}
