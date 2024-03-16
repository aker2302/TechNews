package com.aker.TechNews.model;


import com.aker.TechNews.entity.Article;
import lombok.Data;

import java.util.List;

@Data
public class ResponseModel {
    private String status;
    private int totalResults;
    private List<Article> results;
    private Long nextPage;
}
