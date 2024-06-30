package com.aker.TechNews.Repository;

import com.aker.TechNews.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface ArticleRepository extends MongoRepository<Article, String> {

    @Query("{ '$or': [ { 'title': { '$regex': ?0, '$options': 'i' } }, { 'description': { '$regex': ?0, '$options': 'i' } }, { 'keywords': { '$regex': ?0, '$options': 'i' } } ] }")
    Page<Article> searchByKeywords(String words, Pageable pageable);
}
