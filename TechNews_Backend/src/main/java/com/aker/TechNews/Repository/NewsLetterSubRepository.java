package com.aker.TechNews.Repository;


import com.aker.TechNews.entity.NewsLetterSub;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NewsLetterSubRepository extends MongoRepository<NewsLetterSub, String> {
    Optional<NewsLetterSub> findByEmail(String mail);
}
