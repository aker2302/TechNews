package com.aker.TechNews.model;

import lombok.Data;

import java.util.List;

@Data
public class ArticleModel {
    String article_id;
    String title;
    String link;
    List<String> keywords;
    String description;
    String pubDate;
    String image_url;
    String source_id;
    String source_url;
    String source_icon;
    String source_priority;
    List<String> country;
    List<String> category;
    String language;
}
